package com.aivle.presentation.inputAddress

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
import com.aivle.presentation._base.BaseFragment
import com.aivle.presentation.databinding.FragmentInputAddressBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "InputAddressFragment"

@AndroidEntryPoint
class InputAddressFragment : BaseFragment<FragmentInputAddressBinding>(R.layout.fragment_input_address) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

    }

    private fun checkPermission() {
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
            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
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