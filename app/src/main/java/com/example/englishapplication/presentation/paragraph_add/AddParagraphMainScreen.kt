package com.example.englishapplication.presentation.paragraph_add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private const val MIN_LENGTH = 20

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddParagraphMainScreen(
    viewModel: AddParagraphViewModel,
    onScored: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val text by viewModel.text.collectAsState()
    val phraseResponse by viewModel.phraseResponse.collectAsState()

    val isLoading = uiState is AddParagraphUiState.Loading
    val canSubmit = text.trim().length >= MIN_LENGTH && !isLoading

    LaunchedEffect(uiState, phraseResponse) {
        if (uiState is AddParagraphUiState.Success && phraseResponse != null) {
            viewModel.resetState()
            onScored()
        }
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Chấm điểm đoạn văn",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay lại"
                            )
                        }
                    }
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                "Viết đoạn văn tiếng Anh của bạn",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "AI sẽ chấm điểm, chỉ ra lỗi ngữ pháp và gợi ý bản sửa.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = text,
                onValueChange = viewModel::onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 240.dp),
                enabled = !isLoading,
                placeholder = { Text("Nhập đoạn văn cần chấm điểm...") },
                shape = RoundedCornerShape(20.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                supportingText = {
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            if (text.trim().length < MIN_LENGTH)
                                "Cần tối thiểu $MIN_LENGTH ký tự"
                            else "",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            "${text.length} ký tự",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = viewModel::saveParagraph,
                enabled = canSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Đang chấm điểm...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Chấm điểm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            AnimatedVisibility(visible = uiState is AddParagraphUiState.Error) {
                val msg = (uiState as? AddParagraphUiState.Error)?.msg ?: ""
                Column {
                    Spacer(Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                msg,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
}
