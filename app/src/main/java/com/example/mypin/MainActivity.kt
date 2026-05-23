package com.example.mypin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mypin.presentation.login.LoginScreen
import com.example.mypin.presentation.login.LoginViewModel
import com.example.mypin.presentation.main.MainTabScreen
import com.example.mypin.ui.theme.MyPinTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPinTheme {
                MainContent()
            }
        }
    }
}

@Composable
private fun MainContent() {
    val loginViewModel: LoginViewModel = koinViewModel()
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        MainTabScreen()
    } else {
        LoginScreen(
            uiState = uiState,
            onLogin = { email, password -> loginViewModel.login(email, password) },
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}
