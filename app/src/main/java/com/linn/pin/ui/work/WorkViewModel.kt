package com.linn.pin.ui.work

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linn.pin.data.work.Work
import com.linn.pin.data.work.WorksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkViewModel(private val worksRepository: WorksRepository) : ViewModel() {

    var tabUiState by mutableStateOf(TabUiState())
        private set
    private var _listUiState = MutableStateFlow(ListUiState.Success(listOf()))
    val listUiState: StateFlow<ListUiState> = _listUiState

    init {
        viewModelScope.launch {
            worksRepository.logs(tabUiState.selectedTab.pageSize)
                .collect { items ->
                    _listUiState.value = ListUiState.Success(items)
                }
        }
    }

    suspend fun ding() {
        worksRepository.insertLog()
    }

    fun reloadList(type: WorkTabType) {
        tabUiState = TabUiState(type)
        viewModelScope.launch {
            worksRepository.logs(type.pageSize)
                .collect { items ->
                    _listUiState.value = ListUiState.Success(items)
                }
        }
    }
}

data class TabUiState(val selectedTab: WorkTabType = WorkTabType.FOURTEEN)

enum class WorkTabType(val pageSize: Int, val text: String) {
    FOURTEEN(14, "Latest 14"),
    THIRTY(30, "Latest 30"),
    NINETY(90, "Latest 90")
}

sealed class ListUiState(var itemList: List<Work>) {
    data class Success(val works: List<Work>) : ListUiState(works)
}