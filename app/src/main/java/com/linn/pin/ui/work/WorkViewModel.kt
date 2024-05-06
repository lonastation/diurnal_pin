package com.linn.pin.ui.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linn.pin.data.Work
import com.linn.pin.data.WorksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

class WorkViewModel(worksRepository: WorksRepository) : ViewModel() {
    val workUiState: StateFlow<WorkUiState> =
        worksRepository.getLogs(LocalDateTime.now()).map { WorkUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WorkUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class WorkUiState(val itemList: List<Work> = listOf())