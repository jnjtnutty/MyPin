package com.example.mypin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mypin.presentation.ui.loginscreen.LoginScreen
import com.example.mypin.ui.theme.MyPinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPinTheme {
                LoginScreen()
            }
        }
    }
}
