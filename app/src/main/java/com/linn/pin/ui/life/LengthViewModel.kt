package com.linn.pin.ui.life

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linn.pin.data.length.Length
import com.linn.pin.data.length.LengthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class LengthViewModel(private val lengthRepository: LengthRepository) : ViewModel() {


    var tabUiState by mutableStateOf(TabUiState())
        private set
    var itemUiState by mutableStateOf(ItemUiState(itemDetails = ItemDetails()))
        private set

    private var _listUiState = MutableStateFlow(ListUiState.Success(listOf()))
    val listUiState: StateFlow<ListUiState> = _listUiState

    fun updateItemUiState(itemDetails: ItemDetails) {
        itemUiState = ItemUiState(itemDetails = itemDetails)
    }

    suspend fun insertLength() {
        if (validateInput()) {
            lengthRepository.insert(itemUiState.itemDetails.toLength())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            number1.isNotEmpty() && number1.toDouble() > 0
        }
    }

    fun reloadLengthList(tabType: LengthTabType, filterType: LengthFilterType) {
        tabUiState = TabUiState(tabType, filterType)
        viewModelScope.launch {
            when (tabType) {
                LengthTabType.FIRST -> {
                    when (filterType) {
                        LengthFilterType.ONLY_AM -> lengthRepository.findNumber1AtAm()
                            .collect { items -> _listUiState.value = ListUiState.Success(items) }

                        LengthFilterType.ONLY_PM -> lengthRepository.findNumber1AtPm()
                            .collect { items -> _listUiState.value = ListUiState.Success(items) }

                        else -> lengthRepository.findAll().collect { items ->
                            _listUiState.value = ListUiState.Success(items)
                        }
                    }
                }

                LengthTabType.SECOND ->
                    lengthRepository.findNumber2().collect { items ->
                        _listUiState.value = ListUiState.Success(items)
                    }

                LengthTabType.ALL -> lengthRepository.findAll().collect { items ->
                    _listUiState.value = ListUiState.Success(items)
                }
            }

        }
    }
}

data class TabUiState(
    val selectedTab: LengthTabType = LengthTabType.ALL,
    val selectedFilter: LengthFilterType = LengthFilterType.NONE
)

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
)

data class ItemDetails(
    val number1: String = "",
    val number2: String = ""
)

enum class LengthTabType(val text: String, val selectedText: String) {
    FIRST("NO.1", "NO.1"),
    SECOND("NO.2", "NO.2"),
    ALL("ALL", "ALL")
}

enum class LengthFilterType(val text: String) {
    ONLY_AM("AM"), ONLY_PM("PM"), NONE("ALL")
}

fun ItemDetails.toLength(): Length = Length(
    createTime = LocalDateTime.now(),
    number1 = number1.toDouble(),
    number2 = if (number2.isEmpty()) {
        0.0
    } else {
        number2.toDouble()
    }
)

sealed class ListUiState(var itemList: List<Length>) {
    data class Success(val lengths: List<Length>) : ListUiState(lengths)
}