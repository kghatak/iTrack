package com.dol.itrack.notification.workmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dol.itrack.notification.NotificationUtils

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtils.showNotification(context)
    }
}
