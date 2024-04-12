package com.example.roomview.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class AppBottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : AppBottomBar(
        route = "HOME",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object AgentHome : AppBottomBar(
        route = "AGENT_HOME",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object Discover : AppBottomBar(
        route = "DISCOVER",
        title = "Discover",
        icon = Icons.Default.Explore
    )

    data object Create : AppBottomBar(
        route = "CREATE",
        title = "Create",
        icon = Icons.Default.Add
    )

    data object Manage : AppBottomBar(
        route = "MANAGE",
        title = "Manage",
        icon = Icons.Default.Inbox
    )

    data object Settings : AppBottomBar(
        route = "SETTINGS",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}
