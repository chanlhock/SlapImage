package com.example.slapimage.forz.calculator

import android.app.Application
import com.example.slapimage.forz.calculator.history.HistoryService

class Appx: Application() {
    val historyService: HistoryService by lazy {
        HistoryService(this)
    }
}