package com.example.roomview.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

enum class HouseType(val id: Int, val icon: ImageVector, val description: String) {
    TERRACE(0, Icons.Default.Store, "Terrace"),
    SEMI_D(1, Icons.Default.House, "Semi-D"),
    CONDO(2, Icons.Default.Apartment, "Condo");
}