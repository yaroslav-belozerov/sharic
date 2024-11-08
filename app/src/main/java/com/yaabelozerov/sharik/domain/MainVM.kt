package com.yaabelozerov.sharik.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.RandanDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainState(val userId: Long = 0L, val randans: List<RandanDTO> = emptyList())

class MainVM(val api: ApiService): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun fetchRandans() {
        viewModelScope.launch {
            val randans = api.getArchiveRandansByUserId(_state.value.userId)
            _state.update { it.copy(randans = randans) }
        }
    }
}