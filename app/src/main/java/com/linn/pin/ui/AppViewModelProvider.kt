package com.linn.pin.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.linn.pin.PinApplication
import com.linn.pin.ui.life.GirthViewModel
import com.linn.pin.ui.work.WorkViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            WorkViewModel(pinApplication().container.worksRepository)
        }

        initializer {
            GirthViewModel(pinApplication().container.girthsRepository)
        }
    }
}

fun CreationExtras.pinApplication(): PinApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PinApplication)