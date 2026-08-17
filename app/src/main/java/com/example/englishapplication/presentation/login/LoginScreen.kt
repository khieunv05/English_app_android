package com.example.englishapplication.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LoginScreen(
    viewModel: LoginViewModel,
    onSignUpClick: () -> Unit = {}
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "English Application",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome back! Please login to your account.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = viewModel.usernameTextField.collectAsState().value,
                onValueChange = { viewModel.onUsernameChange(it) },
                label = { Text("Username") },
                placeholder = { Text("Abc") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = MaterialTheme.shapes.medium,

            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.passwordTextField.collectAsState().value,
                onValueChange = {  viewModel.onPasswordChange(it) },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val image = if (viewModel.passwordVisible.collectAsState().value) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { viewModel.onPasswordVisibleChange() }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (viewModel.passwordVisible.collectAsState().value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { /* Handle forgot password */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {viewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Login", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?")
                TextButton(onClick = onSignUpClick) {
                    Text("Sign Up", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
