package com.linn.pin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.linn.pin.ui.life.LifeScreen
import com.linn.pin.ui.work.WorkScreen

@Composable
fun PinNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.GirthList.route,
        modifier = modifier
    ) {
        composable(Screens.GirthList.route) {
            LifeScreen()
        }
        composable(Screens.WorkList.route) {
            WorkScreen()
        }
    }
}