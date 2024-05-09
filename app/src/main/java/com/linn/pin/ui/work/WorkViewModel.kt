package com.linn.pin.ui.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linn.pin.data.work.Work
import com.linn.pin.data.work.WorksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

class WorkViewModel(private val worksRepository: WorksRepository) : ViewModel() {

    val workUiState: StateFlow<WorkUiState> =
        worksRepository.logs(LocalDateTime.now()).map { WorkUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = WorkUiState()
            )

    suspend fun ding() {
        worksRepository.insertLog()
    }
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class WorkUiState(val itemList: List<Work> = listOf())