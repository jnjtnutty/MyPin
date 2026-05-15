package com.example.mypin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mypin.presentation.ui.login.LoginScreen
import com.example.mypin.presentation.viewmodel.LoginViewModel
import com.example.mypin.ui.theme.MyPinTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPinTheme {
                LoginScreen(viewModel = loginViewModel)
            }
        }
    }
}
