package com.dol.itrack.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.RemoteInput

class NotificationReplyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val replyText = getReplyMessage(intent)
        Toast.makeText(context, "Reply received: $replyText", Toast.LENGTH_SHORT).show()
    }

    private fun getReplyMessage(intent: Intent): String? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence("key_text_reply")?.toString()
    }
}
