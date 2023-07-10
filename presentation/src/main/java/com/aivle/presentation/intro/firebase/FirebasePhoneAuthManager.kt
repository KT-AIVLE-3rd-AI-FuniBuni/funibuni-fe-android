package com.aivle.presentation.intro.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.loggi.core_util.extensions.log
import com.loggi.core_util.extensions.loge
import java.text.DecimalFormat
import java.util.Timer
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.concurrent.timer

private const val TAG = "FirebasePhoneAuth"
private const val TIME_OUT = 120L

class FirebasePhoneAuthManager @Inject constructor(
//    activity: Activity
) {

    interface OnPhoneAuthCallback {
        fun onVerificationCompleted(smsCode: String)
        fun onVerificationFailed(e: FirebaseException)
        fun onStarted(phoneNumber: String)
        fun onReStarted(phoneNumber: String)
        fun onTimer(elapsedTimeSec: Int, remainingTimeString: String)
        fun onTimeout()
        fun onPhoneAuthSucceed()
        fun onPhoneAuthFailed()
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var storedVerificationId : String? = null
    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private val verificationCallback = OnVerificationCallback()
    private var userCallback: OnPhoneAuthCallback? = null

    private val timer = PhoneAuthTimer(TIME_OUT.toInt())

    init {
        //initializeFirebaseApp(activity)
    }

    fun init(activity: Activity) = apply {
//        FirebaseApp.initializeApp(activity)
//        auth = FirebaseAuth.getInstance()
        //initializeFirebaseApp(activity)
    }

    fun setOnPhoneAuthCallback(callback: OnPhoneAuthCallback) {
        userCallback = callback
    }

    fun send(activity: Activity, phoneNumber: String) {
        log("send(): phoneNumber=$phoneNumber")
        PhoneAuthProvider.verifyPhoneNumber(
            phoneAuthOptionsBuilder(activity, phoneNumber)
                .build()
        )
    }

    fun resend(activity: Activity, phoneNumber: String) {
        log("resend(): phoneNumber=$phoneNumber")
        if (resendingToken != null) {
            PhoneAuthProvider.verifyPhoneNumber(
                phoneAuthOptionsBuilder(activity, phoneNumber)
                    .setForceResendingToken(resendingToken!!)
                    .build())
        } else {
            send(activity, phoneNumber)
        }
    }

    fun authenticate(activity: Activity, smsCode: String) {
        if (storedVerificationId == null) return
        log("authenticate(): smsCode=$smsCode")

        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, smsCode)
        signInWithPhoneAuthCredential(activity, credential)
    }

    private fun phoneAuthOptionsBuilder(activity: Activity, phoneNumber: String): PhoneAuthOptions.Builder =
        PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(TIME_OUT, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(verificationCallback.apply { sendingPhoneNumber = phoneNumber })

    private fun signInWithPhoneAuthCredential(activity: Activity, credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    val additionalUserInfo = task.result?.additionalUserInfo
                    val authCredential = task.result?.credential
                    userCallback?.onPhoneAuthSucceed()
                    timer.stop()
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                    userCallback?.onPhoneAuthFailed()
                }
            }
    }

    inner class OnVerificationCallback : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        var sendingPhoneNumber: String = ""
        private val decimalFormat = DecimalFormat("00")

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // 번호인증 혹은 기타 다른 인증(구글로그인, 이메일로그인 등) 끝난 상태
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            log("onVerificationCompleted(): smsCode=${credential.smsCode}")
            if (credential.smsCode != null) {
                userCallback?.onVerificationCompleted(credential.smsCode!!)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            loge("onVerificationFailed(): $e")

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
            userCallback?.onVerificationFailed(e)
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            loge("onCodeAutoRetrievalTimeOut(): $p0")
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            log("onCodeSent(): $verificationId, $token")

            if (storedVerificationId == null) {
                userCallback?.onStarted(sendingPhoneNumber)
            } else {
                userCallback?.onReStarted(sendingPhoneNumber)
            }

            storedVerificationId = verificationId
            resendingToken = token

            timer.start { elapsedTimeSec, remainingTimeString, isTimeout ->
                userCallback?.onTimer(elapsedTimeSec, remainingTimeString)

                if (isTimeout) {
                    userCallback?.onTimeout()
                }
            }
        }
    }

    private class PhoneAuthTimer(val timeLimit: Int) {

        private val decimalFormat = DecimalFormat("00")

        var elapsedTimeSec = 0

        private var timer: Timer? = null

        fun start(taskOnUiThread: (elapsedTimeSec: Int, remainingTimeString: String, isTimeout: Boolean) -> Unit) {
            stop()
            timer = getTimer(taskOnUiThread)
        }

        fun stop() {
            timer?.cancel()
            timer = null
            elapsedTimeSec = 0
        }

        private fun getTimer(taskOnUiThread: (elapsedTimeSec: Int, remainingTimeString: String, isTimeout: Boolean) -> Unit): Timer =
            timer(initialDelay = 0L, period = 1000L) {
                val remainingTimeSec = timeLimit - elapsedTimeSec.toLong()

                val hour = TimeUnit.SECONDS.toHours(remainingTimeSec) - TimeUnit.SECONDS.toDays(remainingTimeSec) * 24
                val minute = TimeUnit.SECONDS.toMinutes(remainingTimeSec) - hour * 60
                val second = TimeUnit.SECONDS.toSeconds(remainingTimeSec) - minute * 60

                val remainingTimeString =
                    "${decimalFormat.format(minute)} : ${decimalFormat.format(second)}"

//                activity.runOnUiThread {
                if (elapsedTimeSec == timeLimit) {
                    taskOnUiThread.invoke(elapsedTimeSec, remainingTimeString, true)
                    stop()
                } else {
                    taskOnUiThread.invoke(elapsedTimeSec, remainingTimeString, false)
                    elapsedTimeSec++
                }
//                }
            }
    }

    companion object {

        fun initializeFirebaseApp(context: Context) {
            FirebaseApp.initializeApp(context)
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance() // TODO
            )

        }

        fun signOutFirebaseAuth() {
            Firebase.auth.signOut()
        }
    }
}
