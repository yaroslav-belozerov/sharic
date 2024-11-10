package com.yaabelozerov.sharik

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yaabelozerov.sharik.data.ApiService
import com.yaabelozerov.sharik.data.DataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.yaabelozerov.sharik.domain.MainVM

val appModule = module {
    single { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }
    single {
        Retrofit.Builder().client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build()
        ).baseUrl(Const.BASE_URL).addConverterFactory(MoshiConverterFactory.create(get())).build()
            .create(ApiService::class.java)
    }
    single { DataStore(get()) }
    viewModelOf(::MainVM)
}

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}