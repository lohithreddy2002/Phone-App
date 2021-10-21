package com.example.phoneapp.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.internal.telephony.ITelephony
import com.example.phoneapp.R
import com.example.phoneapp.data.ContactsDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import javax.inject.Inject


@AndroidEntryPoint
class NormalService : Service() {

    @Inject
    lateinit var dao: ContactsDao

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("SoonBlockedPrivateApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val telephonyService: ITelephony

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
