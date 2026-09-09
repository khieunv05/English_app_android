package com.example.englishapplication.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.englishapplication.MainActivity
import com.example.englishapplication.domain.model.WordData
import com.example.englishapplication.util.ReminderReceiver

class ReviewWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val wordsNeedReview = fetchWordsNeedReview(context)

        provideContent {
            GlanceTheme {
                WidgetContent(context,wordsNeedReview)
            }
        }
    }

    @Composable
    private fun WidgetContent(context: Context,words: List<WordData>) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.background)
                .padding(12.dp)
        ) {
            Text(
                text = "Cần ôn tập (${words.size})",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlanceTheme.colors.primary
                )
            )

            Spacer(modifier = GlanceModifier.height(8.dp))

            if (words.isEmpty()) {
                Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Không có từ nào cần ôn 🎉",
                        style = TextStyle(fontSize = 13.sp, color = GlanceTheme.colors.onSurfaceVariant)
                    )
                }
            } else {
                LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                    items(words) { word -> WordRow(context,word) }
                }
            }
        }
    }

    @Composable
    private fun WordRow(context: Context,word: WordData) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(ReminderReceiver.EXTRA_NAVIGATE_TO, ReminderReceiver.ROUTE_REVIEW)
        }
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .background(GlanceTheme.colors.surface)
                .padding(10.dp)
                .clickable(  actionStartActivity(intent)
                )
        ) {
            Text(
                text = word.english,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlanceTheme.colors.onSurface
                )
            )
            Text(
                text = word.vietnamese,
                style = TextStyle(fontSize = 13.sp, color = GlanceTheme.colors.onSurfaceVariant)
            )
        }
    }
    suspend fun updateReviewWidget(context: Context) {
        ReviewWidget().updateAll(context)
    }
}