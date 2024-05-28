package com.linn.pin.ui.life

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linn.pin.data.girth.Girth
import com.linn.pin.data.girth.GirthsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GirthViewModel(private val girthsRepository: GirthsRepository) : ViewModel() {


    var tabUiState by mutableStateOf(TabUiState())
        private set
    var itemUiState by mutableStateOf(ItemUiState(itemDetails = ItemDetails()))
        private set

    private var _listUiState = MutableStateFlow(ListUiState.Success(listOf()))
    val listUiState: StateFlow<ListUiState> = _listUiState

    fun updateItemUiState(itemDetails: ItemDetails) {
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

    fun reloadGirthList(tabType: GirthTabType, filterType: GirthFilterType) {
        tabUiState = TabUiState(tabType, filterType)
        viewModelScope.launch {
            when (tabType) {
                GirthTabType.FIRST -> {
                    when (filterType) {
                        GirthFilterType.ONLY_AM -> girthsRepository.findNumber1AtAm()
                            .collect { items -> _listUiState.value = ListUiState.Success(items) }

                        GirthFilterType.ONLY_PM -> girthsRepository.findNumber1AtPm()
                            .collect { items -> _listUiState.value = ListUiState.Success(items) }

                        else -> girthsRepository.findAll().collect { items ->
                            _listUiState.value = ListUiState.Success(items)
                        }
                    }
                }

                GirthTabType.SECOND ->
                    girthsRepository.findNumber2().collect { items ->
                        _listUiState.value = ListUiState.Success(items)
                    }

                GirthTabType.ALL -> girthsRepository.findAll().collect { items ->
                    _listUiState.value = ListUiState.Success(items)
                }
            }

        }
    }
}

data class TabUiState(
    val selectedTab: GirthTabType = GirthTabType.FIRST,
    val selectedFilter: GirthFilterType = GirthFilterType.ONLY_PM
)

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
)

data class ItemDetails(
    val number1: String = "",
    val number2: String = ""
)

enum class GirthTabType(val text: String, val selectedText: String) {
    FIRST("No.1", "No.1"),
    SECOND("No.2", "No.2"),
    ALL("Mixed", "Mixed")
}

enum class GirthFilterType(val text: String) {
    ONLY_AM("AM"), ONLY_PM("PM"), NONE("ALL")
}

fun ItemDetails.toGirth(): Girth = Girth(
    createTime = LocalDateTime.now(),
    number1 = number1.toDouble(),
    number2 = if (number2.isEmpty()) {
        0.0
    } else {
        number2.toDouble()
    }
)

sealed class ListUiState(var itemList: List<Girth>) {
    data class Success(val girths: List<Girth>) : ListUiState(girths)
}