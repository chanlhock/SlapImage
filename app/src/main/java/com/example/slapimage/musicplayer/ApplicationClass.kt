package com.example.slapimage.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.example.slapimage.mp3tagger.tageditor.di.tagEditorViewModelsModule
import com.example.slapimage.mp3tagger.features.spotify.di.spotifyMainModule
import com.example.slapimage.mp3tagger.features.spotify.di.spotifyServicesModule
import com.example.slapimage.mp3tagger.mediastore.di.mediaStoreViewModelsModule
import com.example.slapimage.mp3tagger.core.di.appCoroutinesScope
import com.example.slapimage.mp3tagger.core.di.appSystemManagers
import com.example.slapimage.mp3tagger.core.di.coreFunctionalitiesModule
import org.koin.core.logger.Level
// XED Editor
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
import com.example.slapimage.xededitor.xededitor.App

class ApplicationClass:Application() {
    companion object{
        const val CHANNEL_ID = "MusicNotification"
        const val PLAY = "play"
        const val NEXT = "next"
        const val PREVIOUS = "previous"
        const val EXIT = "exit"
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

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ApplicationClass)
            modules(mediaStoreViewModelsModule, tagEditorViewModelsModule)
            modules(appSystemManagers, appCoroutinesScope, coreFunctionalitiesModule)
            modules(spotifyMainModule, spotifyServicesModule)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Now Playing Song", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "Needed to Show Notification for Playing Song"
            //for lockscreen -> test this and let me know.
//            notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
//            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
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
            App.Companion.getTempDir().apply {
                if (exists() && listFiles().isNullOrEmpty().not()){ deleteRecursively() }
            }

            SetupEditor.init()
            Mutators.loadMutators()
            AutoSaver.start()

            runCatching { UpdateChecker.checkForUpdates("dev") }

            if (InbuiltFeatures.extensions.state.value){
                Extension.loadExtensions(this@ApplicationClass, GlobalScope)
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