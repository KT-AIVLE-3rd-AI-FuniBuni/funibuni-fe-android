package com.aivle.presentation.intro.sign

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.aivle.presentation.R
import com.aivle.presentation.databinding.FragmentInputAddressBinding
import com.aivle.presentation.databinding.FragmentSignUpInputAddressBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

private const val TAG = "InputAddressFragment"

class SignUpInputAddressFragment : BaseSignFragment<FragmentSignUpInputAddressBinding>(R.layout.fragment_sign_up_input_address) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        initView()
    }

    private fun initView() {
        binding.btnCurrentLocation.setOnClickListener {
            checkLocationPermissions()
        }
    }

    private fun checkLocationPermissions() {
        val listener = object : PermissionListener {
            override fun onPermissionGranted() {
                showToast("onPermissionGranted()")
                getLastLocation()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                showToast("onPermissionGranted(): $deniedPermissions")
                Log.e(TAG, "onPermissionDenied(): $deniedPermissions")
            }
        }

        TedPermission.create()
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .setPermissionListener(listener)
            .check()
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(TAG, "PERMISSION_DENIED")
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                Log.d(TAG, "location=${it}")
                Log.d(TAG, "longitude=${it.longitude}, latitude=${it.latitude}, accuracy=${it.accuracy}, time=${it.time}")
            }
    }

    private fun getLastLocationOld() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(TAG, "PERMISSION_DENIED")
            return
        }
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: return


        Log.d(TAG, "getLastLocation(): longitude=${lastLocation.longitude}, latitude=${lastLocation.latitude}")
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}