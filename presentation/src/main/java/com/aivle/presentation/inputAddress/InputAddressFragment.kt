package com.aivle.presentation.inputAddress

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.aivle.presentation.R
import com.aivle.presentation.base.BaseFragment
import com.aivle.presentation.databinding.FragmentInputAddressBinding
import com.aivle.presentation.util.ext.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.loggi.core_util.extensions.log
import com.loggi.core_util.extensions.logw
import dagger.hilt.android.AndroidEntryPoint

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
                getLastLocation()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                showToast("onPermissionGranted(): $deniedPermissions")
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
            logw("PERMISSION_DENIED")
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                log("location=${it}")
                log("longitude=${it.longitude}, latitude=${it.latitude}, accuracy=${it.accuracy}, time=${it.time}")
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
            logw("PERMISSION_DENIED")
            return
        }
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: return


        log("getLastLocation(): longitude=${lastLocation.longitude}, latitude=${lastLocation.latitude}")
    }
}