package com.example.englishapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishapplication.presentation.add_word.AddWordScreen
import com.example.englishapplication.presentation.add_word.AddWordViewModel
import com.example.englishapplication.presentation.login.LoginScreen
import com.example.englishapplication.presentation.login.LoginViewModel
import com.example.englishapplication.presentation.sign_up.SignUpScreen
import com.example.englishapplication.presentation.sign_up.SignUpViewModel
import com.example.englishapplication.presentation.word_main_screen.WordMainScreen
import com.example.englishapplication.presentation.word_main_screen.WordMainScreenViewModel
import com.example.englishapplication.ui.theme.EnglishApplicationTheme
import com.example.englishapplication.util.AuthEvent
import com.example.englishapplication.util.AuthEventManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authEventManager: AuthEventManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnglishApplicationTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    authEventManager = authEventManager
                )
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
                    navController.navigate("wordMainScreen") {
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
        composable("wordMainScreen") {
            val wordMainScreenViewModel: WordMainScreenViewModel = hiltViewModel()
            WordMainScreen(wordMainScreenViewModel){
                navController.navigate("addNewWord")
            }
        }
        composable("addNewWord") {
            val addWordViewModel: AddWordViewModel = hiltViewModel()
            AddWordScreen(
                viewModel = addWordViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}