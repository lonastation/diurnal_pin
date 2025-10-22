package com.linn.pin.ui.navigation

sealed class Screens(val route: String) {
    data object LengthList : Screens("life")
    data object WorkList : Screens("work")
}