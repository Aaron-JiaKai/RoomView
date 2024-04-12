package com.example.roomview.navgraph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.roomview.ui.widgets.AppBottomBar
import com.example.roomview.views.agent.AgentCreateEventView
import com.example.roomview.views.agent.AgentEventDetailsView
import com.example.roomview.views.user.AgentEventsView
import com.example.roomview.views.user.EditProfileView
import com.example.roomview.views.user.SwitchAgentView
import com.example.soiree.screens.SettingsView


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

        composable(route = AppBottomBar.Create.route) {
            AgentCreateEventView(
                onSuccessCreate = {
                    navController.navigate(AppBottomBar.AgentHome.route)
                },
                paddingValues
            )
        }

        composable(route = AppBottomBar.Settings.route) {
            SettingsView(
                onSignOut = {
                    navController.popBackStack()
                    navController.navigate(Graph.AUTHENTICATION) {
                        popUpTo(Graph.HOME) {
                            inclusive = true
                        }
                    }
                },
                onEdit = {
                    navController.navigate(Routes.EditProfile.route)
                },
                onSwitch = {
                    navController.navigate(Routes.EditProfile.route)
                },
                paddingValues
            )
        }

        composable(route = AgentRoutes.EditProfile.route) {
            EditProfileView(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = AgentRoutes.SwitchAgent.route) {
            SwitchAgentView(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        authNavGraph(navController)
    }
}

sealed class AgentRoutes(val route: String) {

    data object EventDetails : Routes(route = "AGENT_EVENT_INFORMATION/{value}")
    data object EditProfile : Routes(route = "EDIT_PROFILE")
    data object EditEventDetails : Routes(route = "EDIT_EVENT_INFORMATION/{value}")
    data object SwitchAgent : Routes(route = "SWITCH_AGENT")
}

