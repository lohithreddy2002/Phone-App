package com.example.phoneapp

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.internal.telephony.ITelephony
import com.example.phoneapp.data.ContactsDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.reflect.Method
import javax.inject.Inject


class OnCallReceiver : BroadcastReceiver() {
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

@AndroidEntryPoint
class NormalService : Service() {

    @Inject
    lateinit var dao: ContactsDao

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("SoonBlockedPrivateApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var telephonyService: ITelephony

        val number = intent?.getStringExtra("number")

        val tm = this.baseContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val m: Method = tm.javaClass.getDeclaredMethod("getITelephony")

        m.isAccessible = true
        telephonyService = m.invoke(tm) as ITelephony

        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            val value = CoroutineScope(Dispatchers.IO).async(Dispatchers.IO) {
                dao.findContact(number.toString())

            }
            if (value.await() != null) {
                telephonyService.endCall()
            }
        }

        val notification =
            NotificationCompat.Builder(this, "1").setContentTitle("Blocked a Call")
                .setContentText("Blocked a call from the number $number")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, notification)
        }
        return super.onStartCommand(intent, flags, startId)

    }
}

@AndroidEntryPoint
@SuppressLint("NewApi")
class ScreeningService : CallScreeningService() {

    @Inject
    lateinit var dao: ContactsDao

    override fun onScreenCall(details: Call.Details) {

        val string = details.handle.toString()
        Timber.tag("number").e("${string.subSequence(4, string.length)}")
        val notification =
            NotificationCompat.Builder(this@ScreeningService, "1").setContentTitle("Times Up")
                .setContentText("Alarm time")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            val value = CoroutineScope(Dispatchers.IO).async(Dispatchers.IO) {
                dao.findContact(string.subSequence(4, string.length).toString())
            }
            Timber.tag("call").e("${value.await()}")
            if (value.await() != null) {
                respondToCall(details, CallResponse.Builder().setDisallowCall(true).build())
            }
            with(NotificationManagerCompat.from(this@ScreeningService)) {
                // notificationId is a unique int for each notification that you must define
                notify(1, notification)
            }
        }



    }
}