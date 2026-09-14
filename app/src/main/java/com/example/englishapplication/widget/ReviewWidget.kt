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
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
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
import androidx.glance.color.ColorProvider
import com.example.englishapplication.MainActivity
import com.example.englishapplication.R
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
    private fun WidgetContent(context: Context, words: List<WordData>) {
        val reviewIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(ReminderReceiver.EXTRA_NAVIGATE_TO, ReminderReceiver.ROUTE_REVIEW)
        }

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.widget_study_hero_background))
                .padding(10.dp)
        ) {
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .cornerRadius(18.dp)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = GlanceModifier
                        .size(52.dp)
                        .background(Color(0x26FFFFFF))
                        .cornerRadius(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_widget_study_book),
                        contentDescription = "Sách học từ vựng",
                        modifier = GlanceModifier.size(44.dp)
                    )
                }

                Spacer(modifier = GlanceModifier.width(11.dp))

                Column(modifier = GlanceModifier.defaultWeight()) {
                    Text(
                        text = "Ôn tập hôm nay",
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(day = Color.White, night = Color.White)
                        )
                    )
                    Spacer(modifier = GlanceModifier.height(3.dp))
                    Text(
                        text = "${words.size} từ đang chờ bạn",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = ColorProvider(day = Color(0xFFE7E9FF), night = Color(0xFFE7E9FF))
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.width(8.dp))

                Box(
                    modifier = GlanceModifier
                        .background(Color.White)
                        .cornerRadius(50.dp)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .clickable(actionStartActivity(reviewIntent)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ôn ngay",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(day = Color(0xFF4F46E5), night = Color(0xFF4F46E5))
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            if (words.isEmpty()) {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(GlanceTheme.colors.surface)
                        .cornerRadius(16.dp)
                        .clickable(actionStartActivity(reviewIntent)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = GlanceModifier
                                .size(52.dp)
                                .background(GlanceTheme.colors.primaryContainer)
                                .cornerRadius(18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                provider = ImageProvider(R.drawable.ic_widget_study_book),
                                contentDescription = "Hoàn thành ôn tập",
                                modifier = GlanceModifier.size(44.dp)
                            )
                        }
                        Spacer(modifier = GlanceModifier.height(8.dp))
                        Text(
                            text = "Đã hoàn thành!",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = GlanceTheme.colors.onSurface
                            )
                        )
                        Spacer(modifier = GlanceModifier.height(3.dp))
                        Text(
                            text = "Hôm nay không còn từ cần ôn",
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = GlanceTheme.colors.onSurfaceVariant
                            )
                        )
                    }
                }
            } else {
                LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                    items(words) { word -> WordRow(context, word) }

                }
            }
        }
    }

    @Composable
    private fun WordRow(context: Context, word: WordData) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(ReminderReceiver.EXTRA_NAVIGATE_TO, ReminderReceiver.ROUTE_REVIEW)
        }
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .background(GlanceTheme.colors.surface)
                .cornerRadius(15.dp)
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .clickable(actionStartActivity(intent)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = GlanceModifier
                    .width(4.dp)
                    .height(34.dp)
                    .background(GlanceTheme.colors.primary)
                    .cornerRadius(50.dp)
            ) {}

            Spacer(modifier = GlanceModifier.width(9.dp))

            Box(
                modifier = GlanceModifier
                    .size(34.dp)
                    .background(GlanceTheme.colors.primaryContainer)
                    .cornerRadius(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = word.english.firstOrNull()?.uppercase() ?: "?",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.onPrimaryContainer
                    )
                )
            }

            Spacer(modifier = GlanceModifier.width(10.dp))

            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = word.english,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.onSurface
                    )
                )
                Spacer(modifier = GlanceModifier.height(1.dp))
                Text(
                    text = word.vietnamese,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = GlanceModifier.width(6.dp))

            Text(
                text = "›",
                style = TextStyle(
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlanceTheme.colors.primary
                )
            )

        }
    }
    suspend fun updateReviewWidget(context: Context) {
        ReviewWidget().updateAll(context)
    }
}