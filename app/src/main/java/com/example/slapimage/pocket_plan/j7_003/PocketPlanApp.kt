package com.example.slapimage.pocket_plan.j7_003

import android.app.Application

class App5: Application() {

    companion object{
        lateinit var instance: App5 private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}