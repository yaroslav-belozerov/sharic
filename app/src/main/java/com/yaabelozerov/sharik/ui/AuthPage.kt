package com.yaabelozerov.sharik.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaabelozerov.sharik.data.LoginDTO
import com.yaabelozerov.sharik.data.RegisterDTO

@Composable
fun AuthPage(modifier: Modifier, onLogin: (LoginDTO) -> Unit, onRegister: (RegisterDTO) -> Unit) {
    var hasAccount by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    Crossfade(hasAccount) { acc ->
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sharik", fontSize = 64.sp, fontWeight = FontWeight.Bold)
            if (acc) {
                var loginDTO by remember { mutableStateOf(LoginDTO()) }
                val isUsernameValid =
                    remember(loginDTO.username.length) { (loginDTO.username.length in 5..50) }
                var typedUsername by remember { mutableStateOf(false) }
                OutlinedTextField(loginDTO.username,
                    label = { Text("Логин") },
                    enabled = !loading,
                    onValueChange = {
                        loginDTO = loginDTO.copy(username = it)
                        typedUsername = true
                    },
                    isError = !isUsernameValid && typedUsername,
                    supportingText = { if (!isUsernameValid && typedUsername) Text("Логин должен содержать от 5 до 50 символов") }, shape = MaterialTheme.shapes.medium)
                val isPasswordValid =
                    remember(loginDTO.password.length) { (loginDTO.password.length in 8..255) }
                var typedPassword by remember { mutableStateOf(false) }
                OutlinedTextField(loginDTO.password,
                    label = { Text("Пароль") },
                    enabled = !loading,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        loginDTO = loginDTO.copy(password = it)
                        typedPassword = true
                    },
                    isError = !isPasswordValid && typedPassword,
                    supportingText = { if (!isPasswordValid && typedPassword) Text("Пароль должен содержать от 8 до 50 символов") }, shape = MaterialTheme.shapes.medium)
                if (!loading) Button(onClick = {
                    onLogin(loginDTO)
                    loading = true
                }, enabled = !loading && isUsernameValid && isPasswordValid) {
                    Text("Войти")
                } else CircularProgressIndicator()
                TextButton(onClick = { hasAccount = false }, enabled = !loading) {
                    Text("Регистрация")
                }
            } else {
                var registerDTO by remember { mutableStateOf(RegisterDTO()) }
                OutlinedTextField(registerDTO.firstName,
                    label = { Text("Имя") },
                    enabled = !loading,
                    onValueChange = { registerDTO = registerDTO.copy(firstName = it) }, shape = MaterialTheme.shapes.medium)
                OutlinedTextField(registerDTO.lastName,
                    label = { Text("Фамилия") },
                    enabled = !loading,
                    onValueChange = { registerDTO = registerDTO.copy(lastName = it) }, shape = MaterialTheme.shapes.medium)
                val isUsernameValid =
                    remember(registerDTO.username.length) { (registerDTO.username.length in 5..50) }
                var typedUsername by remember { mutableStateOf(false) }
                OutlinedTextField(registerDTO.username,
                    label = { Text("Логин") },
                    enabled = !loading,
                    onValueChange = {
                        registerDTO = registerDTO.copy(username = it)
                        typedUsername = true
                    },
                    isError = !isUsernameValid && typedUsername,
                    supportingText = { if (!isUsernameValid && typedUsername) Text("Логин должен содержать от 5 до 50 символов") }, shape = MaterialTheme.shapes.medium)
                val isPasswordValid =
                    remember(registerDTO.password.length) { (registerDTO.password.length in 8..255) }
                var typedPassword by remember { mutableStateOf(false) }
                OutlinedTextField(registerDTO.password,
                    label = { Text("Пароль") },
                    enabled = !loading,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        registerDTO = registerDTO.copy(password = it)
                        typedPassword = true
                    },
                    isError = !isPasswordValid && typedPassword,
                    supportingText = { if (!isPasswordValid && typedPassword) Text("Пароль должен содержать от 8 до 50 символов") }, shape = MaterialTheme.shapes.medium)
                if (!loading) Button(onClick = {
                    onRegister(registerDTO)
                    loading = true
                }, enabled = !loading && isUsernameValid && isPasswordValid) {
                    Text("Зарегистрироваться")
                } else CircularProgressIndicator()
                TextButton(onClick = { hasAccount = true }, enabled = !loading) {
                    Text("Вход")
                }
            }
        }
    }
}