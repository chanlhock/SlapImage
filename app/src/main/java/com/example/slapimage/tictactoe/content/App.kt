package com.example.slapimage.tictactoe.content

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

internal val Context.settings: DataStore<Preferences> by preferencesDataStore(name = "settings")

internal class App : Application()