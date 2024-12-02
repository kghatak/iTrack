package com.dol.itrack.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.dol.itrack.MainActivity
import java.time.LocalDateTime
import java.util.Timer

object NotificationUtils {
    private const val CHANNEL_ID = "PeriodicNotifications"
    private const val NOTIFICATION_ID = 100

    @SuppressLint("NotificationPermission")
    fun showNotification(context: Context) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java) // Change to your activity
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        // Create an intent for the inline reply action
        val replyIntent = Intent(context, NotificationReplyReceiver::class.java)
        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE // Add FLAG_MUTABLE here
        )


        // Add remote input for text box
        val remoteInput = androidx.core.app.RemoteInput.Builder("key_text_reply")
            .setLabel("Type your reply here") // Placeholder text for the input box
            .build()

        // Add inline reply action
        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val currentDateTime = LocalDateTime.now()
        println("Current DateTime: $currentDateTime")
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Hey What's up'? $currentDateTime")
            .setContentText("What is on your mind?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(replyAction) // Add reply action to the notification

        // Add action buttons
        for (i in 1..5) {
            val buttonIntent = Intent(context, NotificationActionReceiver::class.java).apply {
                putExtra("button_id", i)
            }
            val buttonPendingIntent = PendingIntent.getBroadcast(
                context,
                i,
                buttonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            builder.addAction(0, "B$i", buttonPendingIntent)
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }


    /*fun showNotification(context: Context) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java) // Change to your activity
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Periodic Notification")
            .setContentText("This is your hourly notification.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("This is your hourly notification with additional details.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Add action buttons
        for (i in 1..5) {
            val buttonIntent = Intent(context, NotificationActionReceiver::class.java).apply {
                putExtra("button_id", i)
            }
            val buttonPendingIntent = PendingIntent.getBroadcast(
                context,
                i,
                buttonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.addAction(0, "Button $i", buttonPendingIntent)
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }*/

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Periodic Notifications"
            val description = "Channel for periodic notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
            }

            val notificationManager =
                context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
