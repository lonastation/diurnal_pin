package com.linn.pin.ui.life

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linn.pin.data.Girth
import com.linn.pin.data.GirthsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

class GirthViewModel(private val girthsRepository: GirthsRepository) : ViewModel() {
    val girthUiState: StateFlow<GirthUiState> =
        girthsRepository.findAll().map { GirthUiState(ItemDetails(), it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = GirthUiState()
            )

    fun updateUiState(itemDetails: ItemDetails) {
        girthUiState.value.itemDetails = itemDetails
    }

    suspend fun insertGirth() {
        if (validateInput()) {
            girthsRepository.insert(girthUiState.value.itemDetails.toGirth())
        }
    }

    private fun validateInput(uiState: ItemDetails = girthUiState.value.itemDetails): Boolean {
        return with(uiState) {
            number1 > 0
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class GirthUiState(
    var itemDetails: ItemDetails = ItemDetails(),
    val itemList: List<Girth> = listOf()
)

data class ItemDetails(
    val number1: Double = 0.0,
    val number2: Double = 0.0
)

fun ItemDetails.toGirth(): Girth = Girth(
    createTime = LocalDateTime.now(),
    number1 = number1,
    number2 = number2
)