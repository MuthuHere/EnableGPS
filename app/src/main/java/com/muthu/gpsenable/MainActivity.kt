package com.muthu.gpsenable

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.muthu.gpslib.EnableMyGps
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EnableMyGps.GpsStatusCallBack {

    lateinit var gpsStatus: EnableMyGps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // init
        gpsStatus = EnableMyGps(this)

        // check whether gps is on, if not, request will be happen beyond your class
        gpsStatus.checkMyGpsStatus()

    }


    override fun onGpsSettingStatus(enabled: Boolean) {
        when (enabled) {
            true -> {
                //enabled success do your positive stuff here
            }
            false -> {
                //enabled failed if needed request again
            }
        }
    }

    override fun onGpsAlertCanceledByUser() {
        // cancelled request if needed try request again
    }
}
