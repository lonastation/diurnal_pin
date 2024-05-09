package com.linn.pin.ui.work

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linn.pin.data.work.Work
import com.linn.pin.data.work.WorksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkViewModel(private val worksRepository: WorksRepository) : ViewModel() {

    var tabUiState by mutableStateOf(TabUiState())
        private set
    var listUiState: StateFlow<ListUiState> =
        worksRepository.logs(tabUiState.selectedTab).map { ListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListUiState()
            )

    suspend fun ding() {
        worksRepository.insertLog()
    }

    fun reloadList(count: Int) {
        Log.i("tab index", count.toString())
        tabUiState = TabUiState(count)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ListUiState(var itemList: List<Work> = listOf())

data class TabUiState(val selectedTab: Int = 2)