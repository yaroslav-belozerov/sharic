package com.yaabelozerov.sharik.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.squareup.moshi.Moshi
import com.yaabelozerov.sharik.data.Activity
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.CreateActivityRequest
import com.yaabelozerov.sharik.data.CreateRandanRequest
import com.yaabelozerov.sharik.data.DataStore
import com.yaabelozerov.sharik.data.LoginDTO
import com.yaabelozerov.sharik.data.Owe
import com.yaabelozerov.sharik.data.Pays
import com.yaabelozerov.sharik.data.Randan
import com.yaabelozerov.sharik.data.RegisterDTO
import com.yaabelozerov.sharik.data.User
import com.yaabelozerov.sharik.data.Who
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.awaitResponse
import retrofit2.http.Multipart
import java.io.File
import java.nio.file.Files
import java.util.UUID

data class TotalState(
    val totalProfit: Float = 0f,
    val totalDebt: Float = 0f,
    val peopleProfit: Map<Who, Long> = emptyMap(),
    val peopleDebt: Map<Who, Long> = emptyMap(),
)

data class MainState(
    val randans: List<Randan> = emptyList(), val token: String? = null
)

class MainVM(
    private val api: ApiService, private val dataStore: DataStore, private val moshi: Moshi, private val ctx: Context
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _userState = MutableStateFlow<User?>(null)
    val userState = _userState.asStateFlow()

    private val _totalState = MutableStateFlow(TotalState())
    val totalState = _totalState.asStateFlow()

    private val mediaChoose = MutableStateFlow<(() -> Unit)?>(null)
    fun setMediaChoose(f: () -> Unit) {
        mediaChoose.update { f }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun onPickMedia() {
        mediaChoose.value?.invoke()
    }

    fun onMediaPicker(app: Context, uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                app.contentResolver.openInputStream(uri)?.use {
                    val bitmap = BitmapFactory.decodeStream(it)
                    val dir = File(app.cacheDir, "images")
                    dir.mkdir()
                    val file = File(dir, UUID.randomUUID().toString() + ".jpg")
                    file.createNewFile()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, file.outputStream())
                    val part = MultipartBody.Part.createFormData(
                        "file", file.name, file.asRequestBody()
                    )
                    val resp = api.uploadAvatar(part, _state.value.token!!)
                    editUser(_userState.value!!.copy(avatarUrl = resp.string()))
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            dataStore.getToken().distinctUntilChanged().collect { tok ->
                _state.update { it.copy(token = tok) }
                Log.i("token", tok)
                fetchRandans()
                fetchUser()
            }
        }
    }

    private suspend fun fetchRandans() {
        try {
            val randans = api.getRandansByUser(_state.value.token!!)
            _state.update { it.copy(randans = randans) }
            randans.forEach {
                try {
                    val resp = api.getDebtsByRandan(it.id, _state.value.token!!)
                    resp.othersOweYou.forEach { elem ->
                        viewModelScope.launch {
                            _totalState.update { state ->
                                state.copy(
                                    peopleProfit = state.peopleProfit.plus(
                                        elem.who to state.peopleProfit.getOrDefault(elem.who, 0L)
                                            .plus(elem.amount)
                                    ), totalProfit = state.totalProfit + elem.amount
                                )
                            }
                        }
                    }
                    resp.youOweOthers.forEach { elem ->
                        viewModelScope.launch {
                            _totalState.update { state ->
                                state.copy(
                                    peopleDebt = state.peopleDebt.plus(
                                        elem.who to state.peopleDebt.getOrDefault(elem.who, 0L)
                                            .plus(elem.amount)
                                    ), totalDebt = state.totalDebt + elem.amount
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            Log.w("api", "FetchRandans")
            e.printStackTrace()
        }
    }


    suspend fun fetchUser() {
        try {
            val user = _state.value.token?.let { api.getUser(it) }
            if (user != null) {
                _userState.update { user }
            }
        } catch (e: Exception) {
            Log.w("api", "FetchUser")
            e.printStackTrace()
        }
    }

    private suspend fun setToken(token: String) {
        dataStore.saveToken("Bearer $token")
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

    fun addUserToRandan(randanId: String) {
        viewModelScope.launch {
            _state.value.token?.let {
                try {
                    val body = _userState.value!!.username.toRequestBody("text/plain".toMediaTypeOrNull())
                    val resp = api.addUserToRandan(randanId, body, it)
                    Toast.makeText(ctx, "Вас добавили в ${resp.name}", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } ?: Log.e("addUserToRandan", "token null, randanId $randanId")
        }
    }

    fun sendActivity(request: CreateActivityRequest) {
        viewModelScope.launch {
            _state.value.token?.let { api.createActivity(it, request) }
            fetchRandans()
        }
    }

    fun createRandan(name: String) {
        viewModelScope.launch {
            _state.value.token?.let {
                api.createRandan(
                    CreateRandanRequest(
                        name, emptyList(), emptyList(), emptyList(), false
                    ), it
                )
            } ?: Log.e("createRandan", "token null")
            fetchRandans()
        }
    }

    suspend fun editUser(user: User) {
        _state.value.token?.let {
            api.updateUser(user, it)
            fetchUser()
        }
    }

    fun logout() {
        _state.update { MainState() }
        _userState.update { null }
        _totalState.update { TotalState() }
        viewModelScope.launch {
            dataStore.saveToken("")
        }
    }

    fun refreshAll() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            delay(1000)
            _totalState.update { TotalState() }
            fetchUser()
            fetchRandans()
            _isRefreshing.update { false }
        }
    }
}