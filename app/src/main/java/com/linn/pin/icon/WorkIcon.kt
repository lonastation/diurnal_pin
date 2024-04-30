package com.linn.pin.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.linn.pin.R


@Composable
fun WorkIcon() {
    return Icon(
        painter = painterResource(id = R.drawable.baseline_airplanemode_active_24),
        contentDescription = null
    )
}