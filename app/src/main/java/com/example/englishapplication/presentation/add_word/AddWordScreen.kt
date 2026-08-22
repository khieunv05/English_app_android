package com.example.englishapplication.presentation.add_word

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

private val LEVEL_OPTIONS = listOf("A1", "A2", "B1", "B2", "C1", "C2")

private val PART_OF_SPEECH_OPTIONS = listOf(
    "noun", "verb", "adjective", "adverb", "pronoun", "preposition", "conjunction", "interjection"
)

private enum class PendingAction { NONE, GENERATE, SAVE }

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddWordScreen(
    viewModel: AddWordViewModel,
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val english by viewModel.englishTextField.collectAsState()
    val vietnamese by viewModel.vietnameseTextField.collectAsState()
    val pronunciation by viewModel.pronunciationTextField.collectAsState()
    val partOfSpeech by viewModel.partOfSpeechTextField.collectAsState()
    val level by viewModel.levelTextField.collectAsState()
    val example by viewModel.exampleTextField.collectAsState()
    val exampleTranslation by viewModel.exampleTranslationTextField.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var pendingAction by remember { mutableStateOf(PendingAction.NONE) }
    var isErrorMessage by remember { mutableStateOf(false) }

    val isLoading = uiState is AddWordUiState.Loading
    val isGenerating = isLoading && pendingAction == PendingAction.GENERATE
    val isSaving = isLoading && pendingAction == PendingAction.SAVE
    val canGenerate = english.isNotBlank() && !isLoading
    val canSave = english.isNotBlank() && vietnamese.isNotBlank() && pronunciation.isNotBlank()
            && partOfSpeech.isNotBlank() && level.isNotBlank() && example.isNotBlank()
            && exampleTranslation.isNotBlank()
            && !isLoading

    LaunchedEffect(isLoading) {
        if (!isLoading) pendingAction = PendingAction.NONE
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AddWordUiState.Success -> {
                isErrorMessage = false
                snackbarHostState.showSnackbar(state.message)
            }
            is AddWordUiState.Error -> {
                isErrorMessage = true
                snackbarHostState.showSnackbar(state.message)
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Thêm từ mới",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    shape = RoundedCornerShape(12.dp),
                    containerColor = if (isErrorMessage) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        MaterialTheme.colorScheme.inverseSurface
                    },
                    contentColor = if (isErrorMessage) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        MaterialTheme.colorScheme.inverseOnSurface
                    }
                )
            }
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = {
                            pendingAction = PendingAction.SAVE
                            viewModel.addWord()
                        },
                        enabled = canSave,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Thêm vào danh sách",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard(title = "Từ tiếng Anh", icon = Icons.Default.Abc) {
                OutlinedTextField(
                    value = english,
                    onValueChange = { viewModel.onEnglishTextFieldChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Từ cần thêm") },
                    placeholder = { Text("apple") },
                    singleLine = true,
                    enabled = !isLoading,
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Text(
                    text = "Nhập từ tiếng Anh rồi để AI điền các thông tin còn lại.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

                FilledTonalButton(
                    onClick = {
                        pendingAction = PendingAction.GENERATE
                        viewModel.generateWordInfo()
                    },
                    enabled = canGenerate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Đang tạo thông tin...", fontWeight = FontWeight.SemiBold)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tạo thông tin bằng AI", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            SectionCard(title = "Chi tiết từ vựng", icon = Icons.Default.Translate) {
                OutlinedTextField(
                    value = vietnamese,
                    onValueChange = { viewModel.changeVietnameseText(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nghĩa tiếng Việt") },
                    placeholder = { Text("quả táo") },
                    singleLine = true,
                    enabled = !isLoading,
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = pronunciation,
                    onValueChange = { viewModel.changePronunciationText(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Phiên âm") },
                    placeholder = { Text("ˈæp.əl") },
                    leadingIcon = {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(20.dp))
                    },
                    singleLine = true,
                    enabled = !isLoading,
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = partOfSpeech,
                    onValueChange = { viewModel.changePartOfSpeechText(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Từ loại") },
                    placeholder = { Text("noun") },
                    leadingIcon = {
                        Icon(Icons.Default.Category, contentDescription = null, modifier = Modifier.size(20.dp))
                    },
                    singleLine = true,
                    enabled = !isLoading,
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                SuggestionRow(
                    options = PART_OF_SPEECH_OPTIONS,
                    selected = partOfSpeech,
                    enabled = !isLoading,
                    onSelect = { viewModel.changePartOfSpeechText(it) }
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Trình độ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    SuggestionRow(
                        options = LEVEL_OPTIONS,
                        selected = level,
                        enabled = !isLoading,
                        onSelect = { viewModel.changeLevelText(it) }
                    )
                }
            }

            SectionCard(title = "Câu ví dụ", icon = Icons.Default.FormatQuote) {
                OutlinedTextField(
                    value = example,
                    onValueChange = { viewModel.changeExampleText(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ví dụ tiếng Anh") },
                    placeholder = { Text("I eat an apple every morning.") },
                    minLines = 2,
                    maxLines = 4,
                    enabled = !isLoading,
                    shape = MaterialTheme.shapes.medium,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = exampleTranslation,
                    onValueChange = { viewModel.changeExampleTranslationText(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Dịch câu ví dụ") },
                    placeholder = { Text("Tôi ăn một quả táo mỗi buổi sáng.") },
                    minLines = 2,
                    maxLines = 4,
                    enabled = !isLoading,
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            content()
        }
    }
}


@Composable
private fun SuggestionRow(
    options: List<String>,
    selected: String,
    enabled: Boolean,
    onSelect: (String) -> Unit
) {
    val allOptions = if (selected.isNotBlank() && options.none { it.equals(selected, ignoreCase = true) }) {
        listOf(selected) + options
    } else {
        options
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        allOptions.forEach { option ->
            val isSelected = selected.equals(option, ignoreCase = true)
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(if (isSelected) "" else option) },
                enabled = enabled,
                label = { Text(option, style = MaterialTheme.typography.labelLarge) },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
