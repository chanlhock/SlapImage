package com.example.slapimage.xededitor.xededitor

import android.app.Application
import android.os.Build
import android.os.StrictMode
import com.github.anrwatchdog.ANRWatchDog
import com.example.slapimage.xededitor.crashhandler.CrashHandler
import com.example.slapimage.xededitor.extension.Extension
import com.example.slapimage.xededitor.extension.ExtensionManager
import com.example.slapimage.xededitor.libcommons.application
import com.example.slapimage.xededitor.libcommons.editor.SetupEditor
import com.example.slapimage.xededitor.resources.Res
import com.example.slapimage.xededitor.settings.Settings
import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity
import com.example.slapimage.xededitor.xededitor.MainActivity.tabs.editor.AutoSaver
import com.example.slapimage.xededitor.xededitor.ui.screens.settings.extensions.Extensions
import com.example.slapimage.xededitor.xededitor.ui.screens.settings.feature_toggles.InbuiltFeatures
import com.example.slapimage.xededitor.xededitor.ui.screens.settings.mutators.Mutators
import com.example.slapimage.xededitor.xededitor.update.UpdateChecker
import com.example.slapimage.xededitor.xededitor.update.UpdateManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors
import com.example.slapimage.BuildConfig


class App : Application() {

    companion object {
        fun getTempDir(): File {
            val tmp = File(application!!.filesDir.parentFile, "tmp")
            if (!tmp.exists()) {
                tmp.mkdir()
            }
            return tmp
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        application = this
        Res.application = this

        Thread.setDefaultUncaughtExceptionHandler(CrashHandler)

        if (BuildConfig.DEBUG || Settings.anr_watchdog){
            ANRWatchDog().start()
        }

        if (BuildConfig.DEBUG || Settings.strict_mode){
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder().apply {
                    detectAll()
                    penaltyLog()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                        penaltyListener(Executors.newSingleThreadExecutor()) { violation ->
                            println(violation.message)
                            violation.printStackTrace()
                            violation.cause?.let { throw it }
                            println("vm policy error")
                        }
                    }
                }.build()
            )
        }

        //wait until UpdateManager is done, it should only take few milliseconds
        UpdateManager.inspect()

        GlobalScope.launch(Dispatchers.IO) {
            getTempDir().apply {
                if (exists() && listFiles().isNullOrEmpty().not()){ deleteRecursively() }
            }

            SetupEditor.init()
            Mutators.loadMutators()
            AutoSaver.start()

            runCatching { UpdateChecker.checkForUpdates("dev") }

            if (InbuiltFeatures.extensions.state.value){
                Extension.loadExtensions(this@App, GlobalScope)
            }
        }

    }

    override fun onTrimMemory(level: Int) {
        XEDMainActivity.withContext {
            binding?.viewpager2?.offscreenPageLimit = 1
        }
        ExtensionManager.onLowMemory()
        super.onTrimMemory(level)
    }

}
