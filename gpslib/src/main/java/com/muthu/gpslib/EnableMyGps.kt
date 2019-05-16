package com.muthu.gpslib

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.support.v4.app.Fragment

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

import java.lang.ref.WeakReference

import android.app.Activity.RESULT_OK

class EnableMyGps {

    private var mActivityWeakReference: WeakReference<Activity>? = null
    private var mCallBackWeakReference: WeakReference<GpsStatusCallBack>? = null

    constructor(activity: Activity) {
        this.mActivityWeakReference = WeakReference(activity)
        this.mCallBackWeakReference = WeakReference(activity as GpsStatusCallBack)
    }

    constructor(fragment: Fragment) {
        this.mActivityWeakReference = WeakReference<Activity>(fragment.activity as Activity?)
        this.mCallBackWeakReference = WeakReference(fragment as GpsStatusCallBack)
    }

    fun checkMyGpsStatus() {
        val activity = mActivityWeakReference?.get()
        val callBack = mCallBackWeakReference?.get()
        if (activity == null || callBack == null) {
            return
        }

        if (isGpsEnabled(activity)) {
            callBack.onGpsSettingStatus(true)
        } else {
            setLocationRequest(activity, callBack)
        }
    }

    private fun isGpsEnabled(activity: Activity): Boolean {
        return (activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )
    }


    private fun setLocationRequest(activity: Activity, callBack: GpsStatusCallBack) {
        val mGoogleApiClient = GoogleApiClient.Builder(activity)
            .addApi(LocationServices.API).build()
        mGoogleApiClient.connect()

        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval((30 * 1000).toLong())
            .setFastestInterval((5 * 1000).toLong())

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true) // important!
            .build()

        val result = LocationServices.SettingsApi
            .checkLocationSettings(mGoogleApiClient, locationSettingsRequest)

        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> callBack.onGpsSettingStatus(true)
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(activity, REQUEST_CODE)
                } catch (e: IntentSender.SendIntentException) {
                    callBack.onGpsSettingStatus(false)
                }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> callBack.onGpsSettingStatus(false)
            }

            mGoogleApiClient.disconnect() // If you do not disconnect, causes a memory leak
        }
    }


    fun checkOnActivityResult(requestCode: Int, resultCode: Int) {
        val activity = mActivityWeakReference!!.get()
        val callBack = mCallBackWeakReference!!.get()
        if (activity == null || callBack == null) {
            return
        }

        if (requestCode == EnableMyGps.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                callBack.onGpsSettingStatus(true)
            } else {
                callBack.onGpsSettingStatus(false)
                callBack.onGpsAlertCanceledByUser()
            }
        }
    }

    interface GpsStatusCallBack {
        fun onGpsSettingStatus(enabled: Boolean)

        fun onGpsAlertCanceledByUser()
    }

    companion object {
        private val REQUEST_CODE = 1001
    }

}

