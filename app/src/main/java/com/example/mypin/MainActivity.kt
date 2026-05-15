package com.example.mypin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mypin.presentation.ui.LoginScreen
import com.example.mypin.presentation.ui.MapScreen
import com.example.mypin.ui.theme.MyPinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPinTheme {
                MyPinApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyPinApp(modifier: Modifier = Modifier) {
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }

    if (isLoggedIn) {
        MapScreen()
    } else {
        LoginScreen(
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}
