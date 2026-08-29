package com.example.englishapplication.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainScreenTabs(val route: String, val title: String, val icon: ImageVector) {
    HOME("home", "Trang chủ", Icons.Default.Home),
    WORD("wordFeature", "Từ vựng", Icons.Default.MenuBook),
    PARAGRAPH("paragraph", "Bài viết", Icons.Default.EditNote)
}