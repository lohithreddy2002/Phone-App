package com.example.phoneapp.service

import android.annotation.SuppressLint
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.phoneapp.R
import com.example.phoneapp.data.ContactsDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

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