package com.example.englishapplication.util
import android.content.Context
import androidx.work.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

object ReviewReminderScheduler {
    private const val WORK_NAME = "review_reminder_work"

    fun scheduleDailyReminder(context: Context, reminderTime: LocalTime = LocalTime.of(21, 15)) {
        val now = LocalDateTime.now()
        var nextRun = now.toLocalDate().atTime(reminderTime)
        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1)
        }
        val initialDelay = Duration.between(now, nextRun).toMinutes()

        val request = PeriodicWorkRequestBuilder<ReviewReminderWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun cancelReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}