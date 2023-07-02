package com.aivle.presentation.util.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aivle.presentation.util.ext.showToast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "CameraManager"

class CameraManager(
    private val fragment: Fragment,
    private val listener: CameraListener,
) : DefaultLifecycleObserver {

    val imageBitmap: Bitmap?
        get() = currentPhotoPath?.let { getImageBitmapFrom(it) }

    private var cameraLauncher: ActivityResultLauncher<Intent>? = null
    private var currentPhotoPath: String? = null

    override fun onCreate(owner: LifecycleOwner) {
        Log.d(TAG, "onCreate()")
        cameraLauncher = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                listener.onImageCapture(imageBitmap, currentPhotoPath)
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.d(TAG, "onDestroy()")
        currentPhotoPath?.let { path ->
            // deleteImageFile(path)
        }
        fragment.lifecycle.removeObserver(this)
    }

    fun init() {
        fragment.lifecycle.addObserver(this)
    }

    fun executeCamera() {
        checkCameraPermission {
            dispatchTakePictureIntent()
        }
    }

    private fun checkCameraPermission(action: () -> Unit) {
        TedPermission.create()
            .setPermissions(Manifest.permission.CAMERA)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    action()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    fragment.showToast("Permission denied")
                }
            })
            .check()
    }

    private fun dispatchTakePictureIntent() {
        Log.d(TAG, "dispatchTakePictureIntent()")
        val photoFile = createImageFile()
            ?: return
        Log.d(TAG, "dispatchTakePictureIntent(): photoFile=$photoFile")
        val photoURI = FileProvider.getUriForFile(
            fragment.requireContext(),
            "${fragment.requireContext().applicationContext.packageName}.fileprovider",
            photoFile,
        )
        Log.d(TAG, "dispatchTakePictureIntent(): photoURI=$photoURI")
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }
        Log.d(TAG, "dispatchTakePictureIntent(): photoURI=$photoURI")
        cameraLauncher?.launch(takePictureIntent)
    }

    private fun createImageFile(): File? {
        return try {
            val context = fragment.requireContext()

            // Create an image file name
            val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

            File.createTempFile(
                "JPEG_${timestamp}_",   // prefix
                ".jpg",                 // suffix
                storageDir              // directory
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    private fun deleteImageFile(path: String) {
        try {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getImageBitmapFrom(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    interface CameraListener {
        fun onImageCapture(bitmap: Bitmap?, imageUri: String?)
    }

    companion object {

        fun getImageBitmapFrom(uri: String): Bitmap? {
            return BitmapFactory.decodeFile(uri)
        }

        fun getImageBitmapFrom(context: Context, uri: Uri): Bitmap? {
            return context.contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }

        fun bitmapToFile(context: Context, bitmap: Bitmap?): File? {
            if (bitmap == null) {
                return null
            }
            return try {
//                val filesDir = context.filesDir
//                val imageFile = File(filesDir, "image.jpg")
                val imageFile = createImageFile(context)

                val outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                imageFile
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        private fun createImageFile(context: Context): File? {
            return try {
                // Create an image file name
                val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

                File.createTempFile(
                    "JPEG_${timestamp}_",   // prefix
                    ".jpg",                 // suffix
                    storageDir              // directory
                )
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
}