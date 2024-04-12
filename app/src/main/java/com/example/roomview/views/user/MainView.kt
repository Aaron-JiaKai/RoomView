package com.example.roomview.views.user

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.roomview.navgraph.HomeNavGraph
import com.example.roomview.ui.widgets.AppBottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainView(navController: NavHostController = rememberNavController()) {
    Scaffold(bottomBar = {
        BottomBar(navController = navController)
    }) { padding ->
        HomeNavGraph(navController = navController, paddingValues = padding)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        AppBottomBar.Home,
        AppBottomBar.Discover,
        AppBottomBar.Manage,
        AppBottomBar.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {

        NavigationBar {
            screens.forEach { screen ->
                NavigationBarItem(selected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true, onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(
                        imageVector = screen.icon, contentDescription = "Navigation Icon"
                    )
                }, label = {
                    Text(screen.title)
                })
            }
        }
    }
}
