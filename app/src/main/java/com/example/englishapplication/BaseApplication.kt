package com.example.englishapplication

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory   // ← đổi import
import androidx.work.Configuration
import com.example.englishapplication.permission.post_notification.NotificationHelper
import com.example.englishapplication.util.ReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
        ReminderScheduler.scheduleNext(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}