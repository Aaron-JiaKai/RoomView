package com.example.roomview.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.roomview.views.auth.LoginContent
import com.example.roomview.views.auth.RegisterContent

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.LOGIN.route
    ) {
        composable(route = AuthScreen.LOGIN.route) {
            LoginContent(
                navController,
                onSignUpClick = {
                    navController.navigate(AuthScreen.SIGNUP.route)
                }
            )
        }

        composable(route = AuthScreen.SIGNUP.route) {
            RegisterContent(
                onRegister = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                },
                onBack = {
                    navController.navigate(AuthScreen.LOGIN.route)
                }
            )
        }
    }
}

sealed class AuthScreen(val route: String) {
    data object LOGIN : AuthScreen(route = "LOGIN")
    data object SIGNUP : AuthScreen(route = "SIGN_UP")
}