package com.example.mypin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.mypin.ui.login.LoginScreen
import com.example.mypin.ui.map.MapScreen
import com.example.mypin.ui.theme.MyPinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPinTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyPinApp()
                }
            }
        }
    }
}

@Composable
fun MyPinApp() {
    val isLoggedIn = remember { mutableStateOf(false) }
    val loggedInEmail = remember { mutableStateOf("") }

    if (isLoggedIn.value) {
        MapScreen(email = loggedInEmail.value)
    } else {
        LoginScreen(
            onLoginSuccess = { email ->
                isLoggedIn.value = true
                loggedInEmail.value = email
            }
        )
    }
}
