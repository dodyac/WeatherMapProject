package com.acxdev.weathermapproject.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.acxdev.weathermapproject.ui.Custom
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.lang.Exception

interface LocationListenerX { fun getLocation(location: Location) }

class CurrentLocation(val context: Context, val listenerX: LocationListenerX) {
    fun getLastLocation() {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val request = LocationRequest()
                    request.interval = 10000
                    request.fastestInterval = 5000
                    request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val client = LocationServices.getFusedLocationProviderClient(context)
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) gpsOn()
                    else client.requestLocationUpdates(request, object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            val location: Location = locationResult.lastLocation
                            listenerX.getLocation(location)
                            client.removeLocationUpdates(this)
                        } }, null)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) Custom(context).show()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()

    }

    fun gpsOn() {
        val TAG = "GPS"
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
        builder.setAlwaysShow(true)
        val mLocationSettingsRequest = builder.build()
        val mSettingsClient = LocationServices.getSettingsClient(context)
        mSettingsClient
            .checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener {}
            .addOnCanceledListener {}
            .addOnFailureListener { e: Exception ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val rae = e as ResolvableApiException
                        rae.startResolutionForResult(context as Activity, Constant.LOCATION_REQUEST_CODE)
//                        startIntentSenderForResult(rae.resolution.intentSender, REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null)
                    } catch (sie: Exception) { Log.e(TAG, "Unable to execute request.") }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(TAG,
                        "Location settings are inadequate, and cannot be fixed here. Fix in Settings.")
                }
            }
            .addOnCanceledListener { Log.e(TAG, "checkLocationSettings -> onCanceled") }
    }
}