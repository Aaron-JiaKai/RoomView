package com.example.roomview.navgraph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.roomview.ui.widgets.AppBottomBar
import com.example.roomview.views.user.DiscoverView
import com.example.roomview.views.user.EditProfileView
import com.example.roomview.views.user.EventDetailsView
import com.example.roomview.views.user.EventsView
import com.example.roomview.views.user.ManageEventsView
import com.example.roomview.views.user.SwitchAgentView
import com.example.soiree.screens.SettingsView


/**/

@Composable
fun HomeNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = AppBottomBar.Home.route
    ) {
        composable(route = AppBottomBar.Home.route) {
            EventsView(
                navController = navController
            )
        }

        composable(route = Routes.EventDetails.route + "/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailsView(
                eventId = eventId.toString(),
                onBack = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        composable(route = AppBottomBar.Discover.route) {
            DiscoverView(
                navController = navController, paddingValues
            )
        }

        composable(route = AppBottomBar.Manage.route) {
            ManageEventsView(
                navController = navController
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
                    navController.navigate(Routes.SwitchAgent.route)
                },

                paddingValues
            )
        }

        composable(route = Routes.EditProfile.route) {
            EditProfileView(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.SwitchAgent.route) {
            SwitchAgentView(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        authNavGraph(navController)
    }
}


sealed class Routes(val route: String) {

    data object EventDetails : Routes(route = "EVENT_INFORMATION/{value}")
    data object EditProfile : Routes(route = "EDIT_PROFILE")
    data object SwitchAgent : Routes(route = "SWITCH_AGENT")

}
