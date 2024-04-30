package com.linn.pin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {

    //function to get the list of bottomNavigationItems
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Ding",
                icon = Icons.Filled.Home,
                route = Screens.WorkList.route
            ),
            BottomNavigationItem(
                label = "Life",
                icon = Icons.Filled.Search,
                route = Screens.LifeList.route
            ),
        )
    }
}