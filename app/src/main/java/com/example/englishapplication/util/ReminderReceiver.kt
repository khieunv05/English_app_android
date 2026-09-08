package com.example.englishapplication.util

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.englishapplication.MainActivity
import com.example.englishapplication.R
import com.example.englishapplication.domain.repository.WordRepository
import com.example.englishapplication.permission.post_notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var wordRepository: WordRepository

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("ReminderReceiver", "onReceive chạy lúc ${LocalDateTime.now()}")

                val result = wordRepository.getAllWords()
                result.fold(
                    onSuccess = { words ->
                        val today = LocalDate.now()
                        val needReviewCount = words.flatMap { it.words }
                            .count { it.nextReview == null || it.nextReview.toLocalDate() <= today }

                        Log.d("ReminderReceiver", "needReviewCount = $needReviewCount")

                        if (needReviewCount > 0) {
                            showNotification(context, needReviewCount)
                        }
                    },
                    onFailure = { error ->
                        Log.e("ReminderReceiver", "Lỗi gọi API: ${error.message}", error)
                    }
                )
            } finally {
                ReminderScheduler.scheduleNext(context)
                pendingResult.finish()
            }
        }
    }

    private fun showNotification(context: Context, count: Int) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) return

        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_NAVIGATE_TO, ROUTE_REVIEW)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            REVIEW_NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Đã đến giờ ôn tập!")
            .setContentText("Bạn có $count từ vựng cần ôn tập hôm nay")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(REVIEW_NOTIFICATION_ID, notification)
    }

    companion object {
        const val REVIEW_NOTIFICATION_ID = 1001
        const val EXTRA_NAVIGATE_TO = "extra_navigate_to"
        const val ROUTE_REVIEW = "review"
    }
}