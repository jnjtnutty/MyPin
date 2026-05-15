package com.example.mypin.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Map : Screen("map")
}
