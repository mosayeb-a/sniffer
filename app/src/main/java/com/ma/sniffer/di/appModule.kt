package com.ma.sniffer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ma.sniffer.AppDatabase
import com.ma.sniffer.data.local.PreferencesManager
import com.ma.sniffer.data.repository.FakeNetworkUsageRepository
import com.ma.sniffer.data.repository.NetworkUsageRepositoryImpl
import com.ma.sniffer.domain.repository.NetworkUsageRepository
import com.ma.sniffer.presentation.feature.more.MoreViewModel
import com.ma.sniffer.presentation.feature.usage.UsageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().networkUsageDao() }

    single { PreferencesManager(get()) }
    single<NetworkUsageRepository> { NetworkUsageRepositoryImpl(get()) }

    viewModel { UsageViewModel(get(), get()) }
    viewModel { MoreViewModel(get()) }
}