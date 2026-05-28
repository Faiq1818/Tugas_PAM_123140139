package com.pertemuan5.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.pertemuan5.data.NoteRepository
import com.pertemuan5.data.SettingsRepository
import com.pertemuan5.database.AppDatabase
import com.pertemuan5.platform.DeviceInfo
import com.pertemuan5.platform.NetworkMonitor
import com.pertemuan5.screens.NotesViewModel
import com.pertemuan5.screens.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single {
        val driver = AndroidSqliteDriver(AppDatabase.Schema, androidContext(), "notes.db")
        AppDatabase(driver)
    }

    single { NoteRepository(get()) }
    single { SettingsRepository(androidContext()) }

    // Platform features
    single { DeviceInfo(androidContext()) }
    single { NetworkMonitor(androidContext()) }
}

val viewModelModule = module {
    viewModel { NotesViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}

val appModule = module {
    includes(dataModule, viewModelModule)
}
