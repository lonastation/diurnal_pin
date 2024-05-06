package com.linn.pin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.linn.pin.ui.navigation.BottomNavigationBar

@Composable
fun PinApp(navController: NavHostController = rememberNavController()) {
    BottomNavigationBar(navController)
}