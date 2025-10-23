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
            number.isNotEmpty() && number.toDouble() > 0
        }
    }

    fun reloadLengthList(tabType: LengthTabType, filterType: LengthFilterType) {
        tabUiState = TabUiState(tabType, filterType)
        viewModelScope.launch {
            when (tabType) {
                LengthTabType.CHART -> {
                    when (filterType) {
                        LengthFilterType.LAST_30 -> lengthRepository.findLast30()
                            .collect { items -> _listUiState.value = ListUiState.Success(items) }

                        LengthFilterType.LAST_90 -> lengthRepository.findLast90()
                            .collect { items -> _listUiState.value = ListUiState.Success(items) }

                        else -> lengthRepository.findAll().collect { items ->
                            _listUiState.value = ListUiState.Success(items)
                        }
                    }
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
    val selectedFilter: LengthFilterType = LengthFilterType.ALL
)

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
)

data class ItemDetails(
    val number: String = "",
)

enum class LengthTabType(val text: String, val selectedText: String) {
    CHART("CHART", "CHART"),
    ALL("ALL", "ALL")
}

enum class LengthFilterType(val text: String) {
    LAST_30("30d"), LAST_90("90d"), ALL("ALL")
}

fun ItemDetails.toLength(): Length = Length(
    createTime = LocalDateTime.now(),
    number = number.toFloat()
)

sealed class ListUiState(var itemList: List<Length>) {
    data class Success(val lengths: List<Length>) : ListUiState(lengths)
}