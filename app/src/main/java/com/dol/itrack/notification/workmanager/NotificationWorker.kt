package com.dol.itrack.notification.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dol.itrack.notification.NotificationUtils

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        NotificationUtils.showNotification(applicationContext)
        return Result.success()
    }
}
