package com.app.anonchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.anonchat.ui.auth.LoginScreen
import com.app.anonchat.ui.auth.SignUpScreen
import com.app.anonchat.ui.chat.ChatScreen
import com.app.anonchat.ui.contacts.ContactsScreen
import com.app.anonchat.ui.profile.ProfileSetupScreen
import java.net.URLDecoder
import java.net.URLEncoder

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object ProfileSetup : Screen("profile_setup")
    data object Home : Screen("home")
    data object Chat : Screen("chat/{conversationId}/{otherUsername}")
}

private fun chatRoute(conversationId: String, otherUsername: String): String {
    val encodedUsername = URLEncoder.encode(otherUsername, "UTF-8")
    return "chat/$conversationId/$encodedUsername"
}

@Composable
fun AnonChatNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.ProfileSetup.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.ProfileSetup.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                onProfileReady = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            ContactsScreen(
                onOpenChat = { conversationId, otherUsername ->
                    navController.navigate(chatRoute(conversationId, otherUsername))
                }
            )
        }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
                navArgument("otherUsername") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedUsername = backStackEntry.arguments?.getString("otherUsername") ?: ""
            val otherUsername = URLDecoder.decode(encodedUsername, "UTF-8")
            ChatScreen(otherUsername = otherUsername)
        }
    }
}