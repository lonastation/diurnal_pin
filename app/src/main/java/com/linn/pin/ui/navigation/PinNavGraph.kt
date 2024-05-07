package com.linn.pin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.linn.pin.ui.life.LifeScreen

@Composable
fun PinNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.WorkList.route,
        modifier = modifier
    ) {
        composable(Screens.WorkList.route) {
            LifeScreen(navController = navController)
        }
        composable(Screens.GirthList.route) {
            LifeScreen(navController = navController)
        }
    }
}