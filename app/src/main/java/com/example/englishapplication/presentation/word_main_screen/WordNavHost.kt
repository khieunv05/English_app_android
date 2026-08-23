package com.example.englishapplication.presentation.word_main_screen

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishapplication.presentation.add_word.AddWordScreen
import com.example.englishapplication.presentation.add_word.AddWordViewModel

@Composable
fun WordNavHost(){
    val innerNavController = rememberNavController()

    NavHost(
        navController = innerNavController,
        startDestination = "wordMain"
    ) {
        composable("wordMain") {
            val viewModel: WordMainScreenViewModel = hiltViewModel()
            WordMainScreen(
                viewModel = viewModel,
                onClickAdd = {
                    innerNavController.navigate("addWord")
                }
            )
        }
        composable("addWord") {
            val viewModel: AddWordViewModel = hiltViewModel()
            AddWordScreen(
                viewModel = viewModel,
                onBackClick = { innerNavController.popBackStack() }
            )
        }
    }
}