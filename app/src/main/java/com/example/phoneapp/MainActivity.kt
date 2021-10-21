package com.example.phoneapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.role.RoleManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.phoneapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    private val startActivityForRole =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    Toast.makeText(this, "tst", Toast.LENGTH_SHORT).show()
                }
                RESULT_CANCELED -> {
                    Snackbar.make(binding.root, "Can not Function properly", Snackbar.LENGTH_SHORT)
                        .setAction("retry") {
                            getRole()
                        }.show()
                }
            }
        }


    private val startActivityForPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (keys in it.keys) {
                if (it[keys] != true) {
                    Snackbar.make(binding.root, "Can not Function properly", Snackbar.LENGTH_SHORT)
                        .setAction("retry") {
                            requestPermission()
                        }.show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.e("test")
        getRole()
        requestPermission()
        setSupportActionBar(binding.toolbar)

        val host = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, host.navController)
        notificationChannel()
    }

    val list = mutableListOf<String>()

    private fun hasContactPermission() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasPhoneLog() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CALL_LOG
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasPhoneState() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_PHONE_STATE
    ) == PackageManager.PERMISSION_GRANTED

    fun hasCallPhone() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (!hasContactPermission()) {
            list.add(Manifest.permission.READ_CONTACTS)
        }
        if (!hasPhoneLog()) {
            list.add(Manifest.permission.READ_CALL_LOG)
        }
        if (!hasPhoneState()) {
            list.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (!hasCallPhone()) {
            list.add(Manifest.permission.CALL_PHONE)
        }
        if (list.isNotEmpty()) {
            startActivityForPermission.launch(list.toTypedArray())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(
            this,
            R.id.fragment
        ).navigateUp() || super.onSupportNavigateUp()
    }

    private fun notificationChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "RemiderChannel"
            val descriptionT = "Channel for Remider app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel_id = "1"
            val channel = NotificationChannel(channel_id, name, importance).apply {
                description = descriptionT
            }
            val nofi: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nofi.createNotificationChannel(channel)
        }
    }

    private fun getRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            startActivityForRole.launch(intent)
        }

    }

}