package com.linn.pin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Work : BottomNavItem("work", Icons.Default.Check, "Ding")
    object Life : BottomNavItem("life", Icons.Default.Favorite, "Life")
}

sealed class Screen(val route: String) {
    object Work : Screen("work")
    object Life : Screen("life")
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Work.route
    ) {
        composable(Screen.Work.route) {
            // Work(navController)
        }

        composable(Screen.Life.route) {
            // Life(navController)
        }
    }
}
