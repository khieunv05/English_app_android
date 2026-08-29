package com.example.englishapplication.presentation.paragraph_main

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.englishapplication.presentation.paragraph_add.AddParagraphMainScreen
import com.example.englishapplication.presentation.paragraph_add.AddParagraphViewModel

@Composable
fun ParagraphMainNavHost(){
    val innerController = rememberNavController()

    NavHost(innerController, startDestination = "paragraphMain"){
        composable("paragraphMain") {
            val viewModel : ParagraphMainViewModel = hiltViewModel()
            ParagraphMainScreen(viewModel){
                innerController.navigate("addParagraph")
            }
        }
        composable("addParagraph") {
            val viewModel : AddParagraphViewModel = hiltViewModel()
            AddParagraphMainScreen(viewModel)
        }
    }
}