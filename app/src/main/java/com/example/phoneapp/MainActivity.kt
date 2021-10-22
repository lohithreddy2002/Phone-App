package com.example.phoneapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.role.RoleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.phoneapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onStart() {
        super.onStart()
        setSupportActionBar(binding.toolbar)

        val host = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, host.navController)
        notificationChannel()

    }




    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(
            this,
            R.id.fragment
        ).navigateUp() || super.onSupportNavigateUp()
    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PhoneAppChannel"
            val descriptionT = "Channel for Phone app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = "1"
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionT
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



}