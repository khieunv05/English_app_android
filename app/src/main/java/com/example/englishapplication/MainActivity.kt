package com.example.englishapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishapplication.permission.post_notification.NotificationHelper
import com.example.englishapplication.presentation.add_word.AddWordScreen
import com.example.englishapplication.presentation.add_word.AddWordViewModel
import com.example.englishapplication.presentation.login.LoginScreen
import com.example.englishapplication.presentation.login.LoginViewModel
import com.example.englishapplication.presentation.main.MainScreen
import com.example.englishapplication.presentation.main.MainScreenViewModel
import com.example.englishapplication.presentation.sign_up.SignUpScreen
import com.example.englishapplication.presentation.sign_up.SignUpViewModel
import com.example.englishapplication.ui.theme.EnglishApplicationTheme
import com.example.englishapplication.util.AuthEvent
import com.example.englishapplication.util.AuthEventManager
import com.example.englishapplication.util.NavigationEventManager
import com.example.englishapplication.util.ReviewReminderScheduler
import com.example.englishapplication.util.ReviewReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authEventManager: AuthEventManager

    @Inject
    lateinit var navigationEventManager: NavigationEventManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleNotificationIntent(intent)
        setContent {
            EnglishApplicationTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    authEventManager = authEventManager,

                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNotificationIntent(intent)
    }
    private fun handleNotificationIntent(intent: Intent){
        val route = intent.getStringExtra(ReviewReminderWorker.EXTRA_NAVIGATE_TO)
        if(route == ReviewReminderWorker.ROUTE_REVIEW){
            lifecycleScope.launch {
                navigationEventManager.requestNavigateToReviewTab()
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    authEventManager: AuthEventManager
) {
    LaunchedEffect(Unit) {
        authEventManager.authEvents.collect { event ->
            when (event) {
                is AuthEvent.Unauthorized -> {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                loginViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            ) {
                navController.navigate("signUp") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        composable("signUp") {
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            SignUpScreen(signUpViewModel) {
                navController.navigate("login") {
                    popUpTo("signUp") { inclusive = true }
                }
            }
        }
        composable("main") {
            val mainScreenViewModel: MainScreenViewModel = hiltViewModel()
            MainScreen()
        }
    }
}