package com.linn.pin.ui.navigation

sealed class Screens(val route: String) {
    object WorkList : Screens("work")
    object GirthList : Screens("life")
}