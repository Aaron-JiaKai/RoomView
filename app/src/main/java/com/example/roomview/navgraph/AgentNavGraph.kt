package com.example.roomview.navgraph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.roomview.ui.widgets.AppBottomBar
import com.example.roomview.views.agent.AgentEventDetailsView
import com.example.roomview.views.user.AgentEventsView


@Composable
fun AgentNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        route = Graph.AGENT,
        startDestination = AppBottomBar.AgentHome.route
    ) {
        composable(route = AppBottomBar.AgentHome.route) {
            AgentEventsView(
                navController = navController
            )
        }

        composable(route = AgentRoutes.EventDetails.route + "/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            AgentEventDetailsView(
                eventId = eventId.toString(),
                onBack = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        authNavGraph(navController)
    }
}


sealed class AgentRoutes (val route: String) {

    data object EventDetails : Routes(route = "AGENT_EVENT_INFORMATION/{value}")
    data object EditProfile : Routes(route = "EDIT_PROFILE")
    data object EditEventDetails : Routes(route = "EDIT_EVENT_INFORMATION/{value}")
    data object SwitchAgent : Routes(route = "SWITCH_AGENT")
}


