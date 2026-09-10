package com.app.anonchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.anonchat.ui.auth.LoginScreen
import com.app.anonchat.ui.auth.SignUpScreen

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Home : Screen("home") // placeholder until Phase 2 (profile setup) lands
}

@Composable
fun AnonChatNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home.route) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = { navController.navigate(Screen.Home.route) },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) {
            // Placeholder screen — Phase 2 replaces this with anonymous profile setup.
            com.app.anonchat.ui.HomePlaceholderScreen()
        }
    }
}
