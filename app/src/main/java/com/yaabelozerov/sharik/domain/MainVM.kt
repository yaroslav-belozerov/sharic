package com.yaabelozerov.sharik.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.RandanDTO
import com.yaabelozerov.sharik.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainState(
    val userId: Long = 0L,
    val randans: List<RandanDTO> = emptyList(),
    val cardPreviews: Pair<Float, Float> = Pair(0f, 0f),
    val cardPeople: Pair<List<Pair<User, Float>>, List<Pair<User, Float>>> = Pair(
        emptyList(), emptyList()
    ),
)

class MainVM(val api: ApiService) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        fetchCardValues()
        fetchRandans()
    }

    fun fetchRandans() {
        viewModelScope.launch {
            val randans = api.getCurrentRandansByUserId(_state.value.userId)
            _state.update { it.copy(randans = randans) }
        }
    }

    suspend fun getUserById(id: Long) = api.getUserById(id)

    fun fetchCardValues() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    cardPreviews = Pair(
                        api.totalDebtByUser(_state.value.userId),
                        api.totalProfitByUser(_state.value.userId)
                    )
                )
            }
        }
    }

    fun fetchCardPeople() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    cardPeople = Pair(
                        api.peopleToGiveMoneyByUser(0L), api.peopleToRecieveMoneyByUser(0L)
                    )
                )
            }
        }
    }
}