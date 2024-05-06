package com.linn.pin.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.linn.pin.R
import com.linn.pin.Screens

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {

    @Composable
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Ding",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_airplanemode_active_24),
                route = Screens.WorkList.route
            ),
            BottomNavigationItem(
                label = "Life",
                icon = ImageVector.vectorResource(id = R.drawable.baseline_lunch_dining_24),
                route = Screens.GirthList.route
            ),
        )
    }
}