package com.yaabelozerov.sharik.domain

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.squareup.moshi.Moshi
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.CreateActivityRequest
import com.yaabelozerov.sharik.data.CreateRandanRequest
import com.yaabelozerov.sharik.data.DataStore
import com.yaabelozerov.sharik.data.LoginDTO
import com.yaabelozerov.sharik.data.Randan
import com.yaabelozerov.sharik.data.RegisterDTO
import com.yaabelozerov.sharik.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

class MainVM(private val api: ApiService, private val dataStore: DataStore, private val moshi: Moshi) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    init {
        viewModelScope.launch {
            //fetchCardValues()
            dataStore.getToken().distinctUntilChanged().collect { tok ->
                _state.update { it.copy(token = tok) }
                Log.i("token", tok)
                fetchRandans()
                fetchUser()
            }
        }
    }

    suspend fun fetchRandans() {
        try {
            val randans = api.getRandansByUser(_state.value.token!!)
            _state.update { it.copy(randans = randans) }
        } catch (e: Exception) {
            Log.w("api", "FetchRandans")
            e.printStackTrace()
        }
    }

    suspend fun fetchUser() {
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
            Log.w("api", "FetchUser")
            e.printStackTrace()
        }
    }

    private suspend fun setToken(token: String) {
        dataStore.saveToken("Bearer $token")
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
            try {
                val resp = api.login(loginDTO)
                setToken(resp.token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun register(registerDTO: RegisterDTO) {
        viewModelScope.launch {
            try {
                val resp = api.register(registerDTO)
                setToken(resp.token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

    fun sendActivity(request: CreateActivityRequest) {
        viewModelScope.launch {
            _state.value.token?.let { api.createActivity(it, request) }
        }
    }

    fun createRandan(name: String) {
        viewModelScope.launch {
            _state.value.token?.let {
                api.createRandan(CreateRandanRequest(name, emptyList(), emptyList(), emptyList(), false), it)
            } ?: Log.e("createRandan", "token null")
            fetchRandans()
        }
    }
}