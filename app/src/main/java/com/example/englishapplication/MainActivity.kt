package com.example.englishapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishapplication.presentation.login.LoginScreen
import com.example.englishapplication.presentation.login.LoginViewModel
import com.example.englishapplication.presentation.sign_up.SignUpScreen
import com.example.englishapplication.presentation.sign_up.SignUpViewModel
import com.example.englishapplication.ui.theme.EnglishApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnglishApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login"){
                    composable("login") {
                            val loginViewModel: LoginViewModel = hiltViewModel()
                        LoginScreen(loginViewModel){
                            navController.navigate("signUp"){
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                    composable("signUp") {
                        val signUpViewModel: SignUpViewModel = hiltViewModel()
                        SignUpScreen(signUpViewModel){
                            navController.navigate("login"){
                                popUpTo("signUp") { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }

}
