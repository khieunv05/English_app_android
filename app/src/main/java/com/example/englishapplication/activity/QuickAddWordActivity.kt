package com.example.englishapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.englishapplication.presentation.add_word.AddWordUiState
import com.example.englishapplication.presentation.add_word.AddWordViewModel
import com.example.englishapplication.presentation.add_word.QuickAddWordSheet
import com.example.englishapplication.ui.theme.EnglishApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickAddWordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedText = intent?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString().orEmpty()

        setContent {
            EnglishApplicationTheme {
                val viewModel: AddWordViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(uiState) {
                    if (uiState is AddWordUiState.Success) {
                        finish()
                    }
                }

                QuickAddWordSheet(
                    initialWord = selectedText,
                    viewModel = viewModel,
                    onDismiss = { finish() }
                )
            }
        }
    }
}