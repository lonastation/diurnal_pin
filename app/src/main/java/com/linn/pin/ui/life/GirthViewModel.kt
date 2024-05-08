package com.linn.pin.ui.life

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    val listUiState: StateFlow<ListUiState> =
        girthsRepository.findAll().map { ListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListUiState()
            )
    var itemUiState by mutableStateOf(
        ItemUiState(
            itemDetails = ItemDetails(),
        )
    )

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState = ItemUiState(itemDetails = itemDetails)
    }

    suspend fun insertGirth() {
        if (validateInput()) {
            girthsRepository.insert(itemUiState.itemDetails.toGirth())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            number1.isNotEmpty() && number1.toDouble() > 0
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ListUiState(
    val itemList: List<Girth> = listOf()
)

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
)

data class ItemDetails(
    val number1: String = "",
    val number2: String = ""
)

fun ItemDetails.toGirth(): Girth = Girth(
    createTime = LocalDateTime.now(),
    number1 = number1.toDouble(),
    number2 = number2.toDouble()
)