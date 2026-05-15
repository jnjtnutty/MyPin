package com.example.mypin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mypin.features.login.presentation.ui.LoginScreen
import com.example.mypin.features.login.presentation.viewmodel.LoginViewModel
import com.example.mypin.ui.theme.MyPinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPinTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val loginViewModel: LoginViewModel = viewModel()
                    LoginScreen(viewModel = loginViewModel)
                }
            }
        }
    }
}
