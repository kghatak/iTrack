package com.dol.itrack

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.dol.itrack.databinding.ActivityMainBinding
import com.dol.itrack.db.AppDatabase
import com.dol.itrack.db.models.Event
import com.dol.itrack.db.models.Mood
import com.dol.itrack.notification.foregroundservice.ForegroundNotificationService
import com.dol.itrack.notification.workmanager.NotificationReceiver
import com.dol.itrack.notification.NotificationUtils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    /*** Notification START ***/
    companion object {
        const val POST_NOTIFICATIONS_PERMISSION = "android.permission.POST_NOTIFICATIONS"
    }

    private val NOTIFICATION_PERMISSION_CODE = 1001

    /*** Notification END ***/
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var sharedPreferences: SharedPreferences
    private var isLoggedIn: Boolean = false
    private var userName: String? = null
    private var userEmail: String? = null
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Update", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        loadUserData()

        // Add a listener to handle Profile dynamically
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    showProfileDialog() // Handle profile logic
                    true
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(menuItem, navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }

//        schedulePopup()


        /*** Notification START ***/

        // Check and request notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS_PERMISSION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(POST_NOTIFICATIONS_PERMISSION),
                    NOTIFICATION_PERMISSION_CODE
                )
            } else {
                schedulePeriodicWork() // Permission already granted
            }
        } else {
            schedulePeriodicWork() // Permission not required for older Android versions
        }

        NotificationUtils.showNotification(this)

       // scheduleAlarm(this)

        val serviceIntent = Intent(this, ForegroundNotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

    }

    @SuppressLint("ShortAlarm")
    fun scheduleAlarm(context: Context) {
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, NotificationReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 60 * 1000, // 20 seconds delay
            60 * 1000, //AlarmManager.INTERVAL_HOUR,
            alarmIntent
        )
    }

    private fun schedulePeriodicWork() {
        /*val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.MINUTES).build()

        // Enqueue unique work to avoid duplicates
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "HourlyNotificationWork", // Unique name for the work
            ExistingPeriodicWorkPolicy.UPDATE, // Keep the existing work if already scheduled
            workRequest
        )*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                schedulePeriodicWork() // Permission granted
            } else {
                // Handle the case when the user denies the permission
            }
        }
    }

    /*** Notification END ***/

    private fun loadUserData() {
        // Load data from SharedPreferences
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        userName = sharedPreferences.getString("userName", null)
        userEmail = sharedPreferences.getString("userEmail", null)
    }
    private fun showProfileDialog() {
        // Inflate the dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_profile, null)

        val profileName = dialogView.findViewById<TextView>(R.id.profileName)
        val profileEmail = dialogView.findViewById<TextView>(R.id.profileEmail)
        val loginPrompt = dialogView.findViewById<TextView>(R.id.loginPrompt)
        val loginLogoutButton = dialogView.findViewById<Button>(R.id.loginLogoutButton)

        // Check login status and update UI
        if (isLoggedIn) {
            profileName.text = "Name: $userName"
            profileEmail.text = "Email: $userEmail"
            profileName.visibility = View.VISIBLE
            profileEmail.visibility = View.VISIBLE
            loginPrompt.visibility = View.GONE
            loginLogoutButton.text = "Log Out"
        } else {
            profileName.visibility = View.GONE
            profileEmail.visibility = View.GONE
            loginPrompt.visibility = View.VISIBLE
            loginLogoutButton.text = "Log In"
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Profile")
            .setView(dialogView)
            .setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
            .create()

        // Handle login/logout button click
        loginLogoutButton.setOnClickListener {
            if (isLoggedIn) {
                sharedPreferences.edit()
                    .putBoolean("isLoggedIn", false)
                    .remove("userName")
                    .remove("userEmail")
                    .apply()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            } else {

                sharedPreferences.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("userName", "John Doe")
                    .putString("userEmail", "john.doe@example.com")
                    .apply()
                Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
            }

            loadUserData()
            // Close dialog and refresh UI
            dialog.dismiss()
            showProfileDialog()
        }

        // Create and show the dialog
        dialog.show()
    }

//    private fun schedulePopup() {
//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed(object : Runnable {
//            override fun run() {
//                showPopupDialog()
//                // Schedule the next popup in 1 hour (3600000 ms)
//                handler.postDelayed(this, 5000)
//            }
//        }, 360) // Initial delay of 1 hour
//    }

    private fun showPopupDialog() {
        // Create an AlertDialog Builder

        val dialogView = layoutInflater.inflate(R.layout.hourly_popup, null)
        val emojiRadioGroup = dialogView.findViewById<RadioGroup>(R.id.emojiRadioGroup)
        val builder = AlertDialog.Builder(this)
        val moodMap = mapOf(
            R.id.happyRadioButton to Mood.HAPPY,
            R.id.sadRadioButton to Mood.SAD,
            R.id.angryRadioButton to Mood.ANGRY,
            R.id.excitedRadioButton to Mood.NEUTRAL
        )
        builder.setTitle("Answer These Questions")
            .setView(dialogView) // Set the custom view

            // Set buttons
            .setPositiveButton("Submit") { _, _ ->
                // Retrieve answers

                val selectedRadioButtonId = emojiRadioGroup.checkedRadioButtonId
                val selectedMood = moodMap[selectedRadioButtonId]?: Mood.HAPPY
                val topOfMind = dialogView.findViewById<EditText>(R.id.answer2).text.toString()
                val lastHourActivity = dialogView.findViewById<EditText>(R.id.answer3).text.toString()

                val event = Event(
                    timestamp = System.currentTimeMillis(),
                    mood = selectedMood ,
                    topOfMind = topOfMind,
                    lastHourActivity = lastHourActivity,
                    extra = "Usually a JSON string of app settings"
                )
                addEventToDB(event)
                Toast.makeText(this, "Event added to database", Toast.LENGTH_SHORT).show()

                 }

            .setNegativeButton("Cancel", null)

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getDBInstance() : AppDatabase {
        if (this::database.isInitialized.not()) {
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "app-database"
            ).build()
            return database
        } else {
            return database
        }
    }

    private fun addEventToDB(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            getDBInstance().appDao().insertEvent(event)
        }
    }

    override fun onDestroy() {
        val handler = Handler(Looper.getMainLooper())
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Prevent memory leaks
    }
}