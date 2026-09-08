package com.example.englishapplication.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.englishapplication.permission.post_notification.ExactAlarmPermissionRequest
import com.example.englishapplication.permission.post_notification.NotificationPermissionRequest
import com.example.englishapplication.presentation.paragraph_main.ParagraphMainNavHost
import com.example.englishapplication.presentation.paragraph_main.ParagraphMainScreen
import com.example.englishapplication.presentation.paragraph_main.ParagraphMainViewModel
import com.example.englishapplication.presentation.word_main_screen.WordNavHost
import com.example.englishapplication.util.NavigationEvent

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationEvent by viewModel.navigationEvents.collectAsState()
    LaunchedEffect(navigationEvent) {
            if (navigationEvent is NavigationEvent.NavigationToReviewTab) {
                navController.navigate("${MainScreenTabs.WORD.route}?tab=1") {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
                viewModel.onNavigationHandled()
            }
    }

    NotificationPermissionRequest()
    ExactAlarmPermissionRequest()
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                MainScreenTabs.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = tab.icon, contentDescription = tab.title)
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (currentRoute == tab.route) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            NavHost(
                navController = navController,
                startDestination = MainScreenTabs.HOME.route
            ) {
                composable(MainScreenTabs.HOME.route) {
                    HomeContent(modifier = Modifier.align(Alignment.Center))
                }
                composable(
                    route = "${MainScreenTabs.WORD.route}?tab={tab}",
                    arguments = listOf(navArgument("tab") { defaultValue = 0 })
                ) {
                    WordNavHost(initialTab = it.arguments?.getInt("tab") ?: 0)
                }
                composable(MainScreenTabs.PARAGRAPH.route) {
                    ParagraphMainNavHost()
                }
            }
        }
    }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Trang chủ",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Nội dung sẽ được cập nhật sau",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}