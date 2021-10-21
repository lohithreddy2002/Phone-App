package com.example.phoneapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager

class OnCallBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val service = Intent(context, NormalService::class.java)
            service.putExtra(
                "number",
                intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            )
            context?.startService(service)
        }

    }
}