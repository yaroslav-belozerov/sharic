package com.yaabelozerov.sharik

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.DataStore
import com.yaabelozerov.sharik.domain.MainVM
import com.yaabelozerov.sharik.ui.AuthPage
import com.yaabelozerov.sharik.ui.MainPage
import com.yaabelozerov.sharik.ui.components.SettingPage
import com.yaabelozerov.sharik.ui.widgets.AddRandanWidget
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

enum class Nav(
    val route: String, val iconFilled: ImageVector, val iconOutlined: ImageVector, val title: String
) {
    MAIN("main", Icons.Filled.Home, Icons.Outlined.Home, "Мои кутежи"), SETTINGS(
        "settings", Icons.Filled.Settings, Icons.Outlined.Settings, "Настройки"
    )
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val intentData = intent.data

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val mainVM = getViewModel<MainVM>()

        val openAvatarChooser =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    mainVM.onMediaPicker(this, uri)
                }
            }
        mainVM.setMediaChoose {
            openAvatarChooser.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }

        setContent {
            val navCtrl = rememberNavController()
            var addRandanOpen by remember { mutableStateOf(false) }

            AppTheme {
                if (addRandanOpen) {
                    AddRandanWidget(onDismissRequest = { addRandanOpen = false }, onConfirmation = {
                        mainVM.createRandan(it)
                        addRandanOpen = false
                    })
                }
                val current = navCtrl.currentBackStackEntryAsState().value?.destination?.route
                val token = mainVM.state.collectAsState().value.token
                if (!token.isNullOrBlank()) Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomAppBar {
                            Nav.entries.forEach {
                                val selected = current == it.route
                                NavigationBarItem(selected, icon = {
                                    Icon(
                                        if (selected) it.iconFilled else it.iconOutlined, null
                                    )
                                }, onClick = {
                                    navCtrl.navigate(it.route)
                                })
                            }
                        }
                    },
                    topBar = {
                        Nav.entries.find { it.route == current }?.let {
                            CenterAlignedTopAppBar(title = {
                                Text(it.title)
                            })
                        }
                    },
                    floatingActionButton = {
                        if (current == Nav.MAIN.route) {
                            ExtendedFloatingActionButton(onClick = {
                                addRandanOpen = true
                            }, text = {
                                Text("Кутёж")
                            }, icon = {
                                Icon(Icons.Filled.Add, contentDescription = null)
                            })
                        }
                    }) { innerPadding ->

                    val us = mainVM.userState.collectAsState().value
                    LaunchedEffect(us) {
                        if (us != null) {
                            intentData?.let {
                                it.getQueryParameter("randan_id")?.let { id ->
                                    mainVM.addUserToRandan(id)
                                }
                            }
                        }
                    }

                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navCtrl,
                        startDestination = Nav.MAIN.route
                    ) {
                        composable(Nav.MAIN.route) {
                            MainPage(mainVM) { clipboard.setPrimaryClip(
                                ClipData.newPlainText("simple text", "http://prod.isntrui.ru/invite?randan_id=$it")
                            ) }
                        }

                        composable(Nav.SETTINGS.route) {
                            SettingPage(mainVM)
                        }

                    }
                } else {
                    Scaffold {
                        if (token == "") AuthPage(modifier = Modifier.padding(it),
                            onLogin = { mainVM.login(it) },
                            onRegister = { mainVM.register(it) })
                    }
                }
            }
        }
    }
}
