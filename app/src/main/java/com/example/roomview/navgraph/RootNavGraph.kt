package com.example.roomview.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.roomview.views.agent.AgentMainView
import com.example.roomview.views.user.MainView

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authNavGraph(navController = navController)

        composable(route = Graph.HOME) {
            MainView()
        }

        composable(route = Graph.AGENT) {
            AgentMainView()
        }
    }
}

object Graph {
    const val ROOT = "ROOT_GRAPH"
    const val AUTHENTICATION = "AUTH_GRAPH"
    const val HOME = "HOME_GRAPH"
    const val AGENT = "HOME_GRAPH"
}