package com.yaabelozerov.sharik.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.squareup.moshi.Moshi
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.CreateRandanRequest
import com.yaabelozerov.sharik.data.DataStore
import com.yaabelozerov.sharik.data.LoginDTO
import com.yaabelozerov.sharik.data.Randan
import com.yaabelozerov.sharik.data.RegisterDTO
import com.yaabelozerov.sharik.data.User
import kotlinx.coroutines.Dispatchers
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.awaitResponse
import retrofit2.http.Multipart
import java.io.File
import java.nio.file.Files
import java.util.UUID

data class MainState(
    val userId: Long = 0L,
    val randans: List<Randan> = emptyList(),
    val cardPreviews: Pair<Float, Float> = Pair(0f, 0f),
    val cardPeople: Pair<List<Pair<User, Float>>, List<Pair<User, Float>>> = Pair(
        emptyList(), emptyList()
    ),
    val token: String? = null
)

class MainVM(private val api: ApiService, private val dataStore: DataStore, private val moshi: Moshi) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _userState = MutableStateFlow<User?>(null)
    val userState = _userState.asStateFlow()

    private val mediaChoose = MutableStateFlow<(() -> Unit)?>(null)
    fun setMediaChoose(f: () -> Unit) {
        mediaChoose.update { f }
    }

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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 1, file.outputStream())
                    val part = MultipartBody.Part.createFormData("file", file.name,
                        file.asRequestBody()
                    )
                    val resp = api.uploadAvatar(part, _state.value.token!!)
                    editUser(_userState.value!!.copy(avatarUrl = resp.string()))
                    fetchUser()
                }
            }
        }
    }

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
                    user
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

    fun createRandan(name: String) {
        viewModelScope.launch {
            _state.value.token?.let {
                api.createRandan(CreateRandanRequest(name, emptyList(), emptyList(), emptyList(), false), it)
            } ?: Log.e("createRandan", "token null")
            fetchRandans()
        }
    }

    private suspend fun editUser(user: User) {
        _state.value.token?.let {
            api.updateUser(user, it)
        }
    }
}