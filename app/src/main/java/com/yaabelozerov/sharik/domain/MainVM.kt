package com.yaabelozerov.sharik.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.DataStore
import com.yaabelozerov.sharik.data.LoginDTO
import com.yaabelozerov.sharik.data.Randan
import com.yaabelozerov.sharik.data.RegisterDTO
import com.yaabelozerov.sharik.data.User
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
    val randans: List<Randan> = emptyList(),
    val cardPreviews: Pair<Float, Float> = Pair(0f, 0f),
    val cardPeople: Pair<List<Pair<User, Float>>, List<Pair<User, Float>>> = Pair(
        emptyList(), emptyList()
    ),
    val token: String? = null
)

data class UserState(
    val name: String? = null,
    val surname: String? = null
)

class MainVM(private val api: ApiService, private val dataStore: DataStore) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchToken()
            //fetchCardValues()
            fetchRandans()
            fetchUser()
        }
    }

    private suspend fun fetchRandans() {
        try {
            val randans = api.getCurrentRandansByUserId(_state.value.userId, _state.value.token!!)
            _state.update { it.copy(randans = randans) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun fetchUser() {
        try {
            val user = _state.value.token?.let { api.getUser(it) }
            if (user != null) {
                _userState.update { state ->
                    state.copy(
                        name = user.firstName,
                        surname = user.lastName
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    fun addUserToRandan(randanId: Long) {
        viewModelScope.launch {
            _state.value.token?.let {
                api.addUserToRandan(randanId, it)
            } ?: Log.e("addUserToRandan", "token null, randanId $randanId")
            //api.addUserToRandan(0L, _state.value.token!!)
        }
    }
}