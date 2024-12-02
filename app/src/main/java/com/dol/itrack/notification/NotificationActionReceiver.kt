package com.dol.itrack.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val buttonId = intent.getIntExtra("button_id", -1)
        Toast.makeText(context, "Button $buttonId clicked", Toast.LENGTH_SHORT).show()
    }
}
