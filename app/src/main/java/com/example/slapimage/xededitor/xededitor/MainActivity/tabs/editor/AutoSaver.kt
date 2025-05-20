package com.example.slapimage.xededitor.xededitor.MainActivity.tabs.editor

import com.example.slapimage.xededitor.settings.Settings
import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
object AutoSaver {
    init {
        GlobalScope.launch(Dispatchers.Default) {
            while (isActive) {
                if (XEDMainActivity.activityRef.get() == null ||
                    XEDMainActivity.activityRef.get()!!.isPaused == true ||
                    XEDMainActivity.activityRef.get()!!.isFinishing ||
                    XEDMainActivity.activityRef.get()!!.isDestroyed) {
                    if (Settings.auto_save) {
                        delay(1000)
                    }else{
                        delay(10000)
                    }
                    continue
                }
                try {
                    // Get the auto-save delay from preferences or use the default
                    delay(Settings.auto_save_interval.toLong())

                    if (Settings.auto_save) {
                       saveAllFiles()
                    }
                } catch (e: Exception) {
                    println("Error in AutoSaver: ${e.message}")
                }
            }
        }
    }
    /**
     * Starts the auto-save process. Runs in a background coroutine, periodically checking
     * if auto-save is enabled and saving the open editor fragments.
     * Auto-Saver survives activity lifecycles so do not put context here
     */

    fun start() {
        //intentionally empty
    }


}
