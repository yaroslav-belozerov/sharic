package com.yaabelozerov.sharik.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.DataStore
import com.yaabelozerov.sharik.data.LoginDTO
import com.yaabelozerov.sharik.data.RandanDTO
import com.yaabelozerov.sharik.data.RegisterDTO
import com.yaabelozerov.sharik.data.UserDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

data class MainState(
    val userId: Long = 0L,
    val randans: List<RandanDTO> = emptyList(),
    val cardPreviews: Pair<Float, Float> = Pair(0f, 0f),
    val cardPeople: Pair<List<Pair<UserDTO, Float>>, List<Pair<UserDTO, Float>>> = Pair(
        emptyList(), emptyList()
    ),
    val token: String? = null
)

class MainVM(private val api: ApiService, private val dataStore: DataStore) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            fetchToken()
            fetchCardValues()
            fetchRandans()
        }
    }

    private suspend fun fetchRandans() {
        val randans = api.getCurrentRandansByUserId(_state.value.userId, _state.value.token!!)
        _state.update { it.copy(randans = randans) }
    }

    private suspend fun fetchToken() {
        dataStore.getToken().first().let {
            _state.update { state ->
                state.copy(token = it)
            }
        }
    }

    suspend fun getUserById(id: Long) = api.getUserById(id, _state.value.token!!)

    private suspend fun setToken(token: String) {
        dataStore.saveToken(token)
        _state.update { state ->
            state.copy(token = token)
        }
    }

    private suspend fun fetchCardValues() {
        _state.update {
            it.copy(
                cardPreviews = Pair(
                    api.totalDebtByUser(_state.value.userId, _state.value.token!!),
                    api.totalProfitByUser(_state.value.userId, _state.value.token!!)
                )
            )
        }
    }

    fun fetchCardPeople() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    cardPeople = Pair(
                        api.peopleToGiveMoneyByUser(0L, _state.value.token!!),
                        api.peopleToRecieveMoneyByUser(0L, _state.value.token!!)
                    )
                )
            }
        }
    }

    fun login(loginDTO: LoginDTO) {
        viewModelScope.launch {
            setToken("token!!!")
//            val resp = api.login(loginDTO).awaitResponse()
//            if (resp.code() == 200) {
//                setToken(resp.body()!!.token)
//            } else {
//                Log.e("login", "${resp.code()} ${resp.errorBody()}")
//            }
        }
    }

    fun register(registerDTO: RegisterDTO) {
        viewModelScope.launch {
            setToken("token <3")
//            val resp = api.register(registerDTO).awaitResponse()
//            if (resp.code() == 200) {
//                setToken(resp.body()!!.token)
//            } else {
//                Log.e("register", "${resp.code()} ${resp.errorBody()}")
//            }
        }
    }
}