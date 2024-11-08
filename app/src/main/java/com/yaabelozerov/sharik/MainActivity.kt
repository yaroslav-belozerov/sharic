package com.yaabelozerov.sharik

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yaabelozerov.sharik.ui.theme.SharikTheme
import org.koin.dsl.module

val appModule = module {
    single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
}

enum class Nav(val route: String, val iconFilled: ImageVector, val iconOutlined: ImageVector) {
    MAIN("main", Icons.Filled.Home, Icons.Outlined.Home)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navCtrl = rememberNavController()

            SharikTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(modifier = Modifier.padding(innerPadding), navController = navCtrl, startDestination = Nav.MAIN.route) {
                        composable(Nav.MAIN.route) {

                        }
                    }
                }
            }
        }
    }
}
