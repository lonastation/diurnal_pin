package com.linn.pin.ui.navigation

sealed class Screens(val route: String) {
    data object WorkList : Screens("work")
    data object GirthList : Screens("life")
}