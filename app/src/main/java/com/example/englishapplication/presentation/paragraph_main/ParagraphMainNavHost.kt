package com.example.englishapplication.presentation.paragraph_main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.englishapplication.presentation.paragraph_add.AddParagraphMainScreen
import com.example.englishapplication.presentation.paragraph_add.AddParagraphViewModel
import com.example.englishapplication.presentation.paragraph_detail.ParagraphDetail

@Composable
fun ParagraphMainNavHost(){
    val innerController = rememberNavController()

    NavHost(innerController, startDestination = "paragraphMain"){
        composable("paragraphMain") {
            val viewModel : ParagraphMainViewModel = hiltViewModel()
            ParagraphMainScreen(viewModel,
                onClickParagraph = {
                    id -> innerController.navigate("paragraphDetail?phraseId=$id")
                }){
                innerController.navigate("addParagraph")
            }
        }
        composable("addParagraph") {
            val viewModel: AddParagraphViewModel = hiltViewModel()
            AddParagraphMainScreen(
                viewModel = viewModel,
                onScored = { innerController.navigate("paragraphDetail") },
                onBack = { innerController.popBackStack() }
            )
        }
        composable(
            route = "paragraphDetail?phraseId={phraseId}",
            arguments = listOf(navArgument("phraseId") { type = NavType.LongType; defaultValue = -1L })
        ) { entry ->
            val phraseId = entry.arguments?.getLong("phraseId")
            // reuse the AddParagraph VM instance that holds the scored result
            if(phraseId == -1L){
                val parentEntry = remember(entry) {
                    innerController.getBackStackEntry("addParagraph")
                }
                ParagraphDetail(
                    viewModel = hiltViewModel<AddParagraphViewModel>(parentEntry),
                    onBack = { innerController.popBackStack("paragraphMain", inclusive = false) }
                )
            }
            else{
                val viewModel : AddParagraphViewModel = hiltViewModel()
                ParagraphDetail(
                    viewModel,
                    onBack = { innerController.popBackStack("paragraphMain", inclusive = false) }
                )
            }

        }
    }
}