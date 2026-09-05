package com.example.englishapplication.util

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.englishapplication.MainActivity
import com.example.englishapplication.domain.repository.WordRepository
import com.example.englishapplication.R
import com.example.englishapplication.permission.post_notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class ReviewReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val wordRepository: WordRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val result = wordRepository.getAllWords()
        return result.fold(
            onSuccess = {
                words->
                val today = LocalDate.now()
                val needReviewCount = words.flatMap {
                    it.words
                }.count{
                    word->
                    word.nextReview == null || word.nextReview.toLocalDate() <= today
                }
                if(needReviewCount>0){
                    showNotification(needReviewCount)
                }
                Result.success()
            },
            onFailure = { Result.retry()}
        )
    }
    private fun showNotification(count: Int){
        val hasPermission = ContextCompat.checkSelfPermission(
            applicationContext, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if(!hasPermission) return
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_NAVIGATE_TO,ROUTE_REVIEW)
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            REVIEW_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(applicationContext, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Đã đến giờ ôn tập!")
            .setContentText("Bạn có $count từ vựng cần ôn tập hôm nay")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        val manager = applicationContext.getSystemService(NotificationManager::class.java)
        manager.notify(REVIEW_NOTIFICATION_ID,notification)
    }
    companion object {
        const val REVIEW_NOTIFICATION_ID = 1001
        const val EXTRA_NAVIGATE_TO = "extra_navigate_to"
        const val ROUTE_REVIEW = "review"
    }
}