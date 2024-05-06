package com.linn.pin

sealed class Screens(val route: String) {
    object WorkList : Screens("work")
    object GirthList : Screens("life")
}