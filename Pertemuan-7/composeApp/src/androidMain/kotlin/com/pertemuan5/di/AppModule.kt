package com.pertemuan5.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.pertemuan5.data.NoteRepository
import com.pertemuan5.data.SettingsRepository
import com.pertemuan5.database.AppDatabase
import com.pertemuan5.screens.NotesViewModel
import com.pertemuan5.screens.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        val driver = AndroidSqliteDriver(AppDatabase.Schema, androidContext(), "notes.db")
        AppDatabase(driver)
    }
    
    single { NoteRepository(get()) }
    single { SettingsRepository(androidContext()) }
    
    viewModel { NotesViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}
