package ru.kpfu.itis.paramonov.androidtasks

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.paramonov.androidtasks.databinding.ActivityMainBinding
import ru.kpfu.itis.paramonov.androidtasks.util.AirplaneModeBroadcastReceiver
import ru.kpfu.itis.paramonov.androidtasks.util.CoroutineHandler
import ru.kpfu.itis.paramonov.androidtasks.util.NotificationHandler
import ru.kpfu.itis.paramonov.androidtasks.util.NotificationHandler.Companion.NOTIFICATION_INTENT_ACTION
import ru.kpfu.itis.paramonov.androidtasks.util.NotificationHandler.Companion.NOTIFICATION_INTENT_ACTION_SETTINGS
import ru.kpfu.itis.paramonov.androidtasks.util.NotificationHandler.Companion.NOTIFICATION_INTENT_ACTION_TOAST
import ru.kpfu.itis.paramonov.androidtasks.util.AirplaneModeBroadcastReceiver.Companion.AIRPLANE_MODE_ON
import ru.kpfu.itis.paramonov.androidtasks.util.AirplaneModeBroadcastReceiver.Companion.AIRPLANE_MODE_OFF
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    private var coroutineHandler = CoroutineHandler(this)

    private var broadcastReceiver: AirplaneModeBroadcastReceiver? = null

    private var isAirplaneModeOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission()
        NotificationHandler(this).createNotificationChannels()

        broadcastReceiver = AirplaneModeBroadcastReceiver()
        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(broadcastReceiver, filter)

        broadcastReceiver?.setOnAirplaneModeChangedListener(getOnAirplaneModeChangedListener())

        checkAirplaneModeOnStart()

        findViewById<BottomNavigationView>(R.id.bnv_main).apply {
            val controller = (supportFragmentManager.findFragmentById(R.id.main_activity_container)
                    as NavHostFragment).navController
            setupWithNavController(controller)
            checkIntent(intent)
        }
    }

    private fun checkAirplaneModeOnStart() {
        isAirplaneModeOn = Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        if (isAirplaneModeOn) onAirplaneModeOn()
    }

    private fun getOnAirplaneModeChangedListener() = object : AirplaneModeBroadcastReceiver.OnAirplaneModeChangedListener {
        override fun onAirplaneModeChanged(airplaneModeStatus: String) {
            when(airplaneModeStatus) {
                AIRPLANE_MODE_ON -> onAirplaneModeOn()
                AIRPLANE_MODE_OFF -> onAirplaneModeOff()
            }
        }
    }

    private fun onAirplaneModeOn() {
        isAirplaneModeOn = true
        binding.layoutAirplaneModeOn.visibility = View.VISIBLE
        findViewById<Button>(R.id.btn_execute_coroutines)?.isEnabled = false
        findViewById<Button>(R.id.btn_create_notif)?.isEnabled = false
    }

    private fun onAirplaneModeOff() {
        isAirplaneModeOn = false
        binding.layoutAirplaneModeOn.visibility = View.GONE
        findViewById<Button>(R.id.btn_execute_coroutines)?.isEnabled = true
        findViewById<Button>(R.id.btn_create_notif)?.isEnabled = true
    }

    fun isAirplaneModeOn(): Boolean {
        return isAirplaneModeOn
    }

    private fun checkIntent(intent: Intent?) {
        val action = intent?.getStringExtra(NOTIFICATION_INTENT_ACTION)
        action?.let {
            when(it) {
                NOTIFICATION_INTENT_ACTION_TOAST -> {
                    Toast.makeText(this, R.string.surprise, Toast.LENGTH_LONG).show()
                }

                NOTIFICATION_INTENT_ACTION_SETTINGS -> {
                    with(findViewById<BottomNavigationView>(R.id.bnv_main)) {
                        findViewById<View>(R.id.notif_settings_fragment)
                            .performClick()
                    }
                }

                else -> throw RuntimeException(getString(R.string.unsupported_action))
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntent(intent)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionWithRationale()
            }
        }
    }

    private fun requestNotificationPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
            Snackbar.make(binding.root, R.string.enable_notifications, Snackbar.LENGTH_LONG)
                .setAction(R.string.agree) {
                    requestNotificationPermission()
                }
                .show()
        } else {
            requestNotificationPermission()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATIONS_PERMISSION_REQ_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allowed = false

        when(requestCode) {
            NOTIFICATIONS_PERMISSION_REQ_CODE -> {
                allowed = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }

        if (!allowed) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                requestNotificationPermissionWithRationale()
            } else {
                requestNotificationPermissionFromSettings()
            }
        }

    }

    private fun requestNotificationPermissionFromSettings() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.enable_notifications))
            .setMessage(getString(R.string.enable_notif_info))
            .setPositiveButton(R.string.go_to_settings) { p0, p1 -> openApplicationSettings() }
            .show()
    }

    private fun openApplicationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
        startActivity(intent)
    }

    fun executeCoroutines(handler: CoroutineHandler) {
        coroutineHandler = handler
        coroutineHandler.execute()

    }

    override fun onStop() {
        super.onStop()
        coroutineHandler.notifyActivityStopped()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        unregisterReceiver(broadcastReceiver)
        broadcastReceiver = null
    }

    companion object {
        private const val NOTIFICATIONS_PERMISSION_REQ_CODE = 1
    }
}