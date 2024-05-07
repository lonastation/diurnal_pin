package com.linn.pin.ui.life

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linn.pin.data.Girth
import com.linn.pin.data.GirthsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GirthViewModel(private val girthsRepository: GirthsRepository) : ViewModel() {
    val girthUiState: StateFlow<GirthUiState> =
        girthsRepository.findAll().map { GirthUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = GirthUiState()
            )

    suspend fun insertGirth(girth: Girth) {
        girthsRepository.insert(girth)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class GirthUiState(val itemList: List<Girth> = listOf())