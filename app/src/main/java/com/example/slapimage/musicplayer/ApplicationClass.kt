package com.example.slapimage.musicplayer

// XED Editor
//import androidx.multidex.MultiDexApplication
// Librera
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.example.slapimage.BuildConfig
import com.example.slapimage.ibook.foobnix.android.utils.Dips
import com.example.slapimage.ibook.foobnix.android.utils.LOG
import com.example.slapimage.ibook.foobnix.android.utils.TxtUtils
import com.example.slapimage.ibook.foobnix.ext.CacheZipUtils
import com.example.slapimage.ibook.foobnix.hypen.HypenUtils
import com.example.slapimage.ibook.foobnix.pdf.info.AppsConfig
import com.example.slapimage.ibook.foobnix.pdf.info.IMG
import com.example.slapimage.ibook.foobnix.pdf.info.Prefs
import com.example.slapimage.ibook.foobnix.pdf.info.TintUtil
import com.example.slapimage.ibook.foobnix.tts.TTSNotification
import com.example.slapimage.mp3tagger.core.di.appCoroutinesScope
import com.example.slapimage.mp3tagger.core.di.appSystemManagers
import com.example.slapimage.mp3tagger.core.di.coreFunctionalitiesModule
import com.example.slapimage.mp3tagger.features.spotify.di.spotifyMainModule
import com.example.slapimage.mp3tagger.features.spotify.di.spotifyServicesModule
import com.example.slapimage.mp3tagger.mediastore.di.mediaStoreViewModelsModule
import com.example.slapimage.mp3tagger.tageditor.di.tagEditorViewModelsModule
import com.example.slapimage.xededitor.crashhandler.CrashHandler
import com.example.slapimage.xededitor.extension.Extension
import com.example.slapimage.xededitor.extension.ExtensionManager
import com.example.slapimage.xededitor.libcommons.application
import com.example.slapimage.xededitor.libcommons.editor.SetupEditor
import com.example.slapimage.xededitor.resources.Res
import com.example.slapimage.xededitor.settings.Settings
import com.example.slapimage.xededitor.xededitor.App
import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity
import com.example.slapimage.xededitor.xededitor.MainActivity.tabs.editor.AutoSaver
import com.example.slapimage.xededitor.xededitor.ui.screens.settings.feature_toggles.InbuiltFeatures
import com.example.slapimage.xededitor.xededitor.ui.screens.settings.mutators.Mutators
import com.example.slapimage.xededitor.xededitor.update.UpdateChecker
import com.example.slapimage.xededitor.xededitor.update.UpdateManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.io.File
import java.util.concurrent.Executors
import com.example.slapimage.forz.calculator.history.HistoryService

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
        lateinit var context: Context   // Librera
        const val CHANNEL_ID_SUDOKU = "sudoku.0" // Sudoku

    }

    // Forz Calculator
    val historyService: HistoryService by lazy {
        HistoryService(this)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        /*if (false) {    // Librera
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build())
        }*/
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

     //   if (BuildConfig.DEBUG || Settings.anr_watchdog){
     //       ANRWatchDog().start()
     //   }

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

        // Librera Application Initialization
       context = applicationContext
        AppsConfig.init(applicationContext)
        Dips.init(null)
        Prefs.get().init(applicationContext)
      //  if (AppsConfig.IS_TEST_DEVICE) {
   //         val configuration = RequestConfiguration.Builder()
    //            .setTestDeviceIds(AppsConfig.testDevices)
    //            .build()
    //        MobileAds.setRequestConfiguration(configuration)
   //     }
        TTSNotification.initChannels(applicationContext)

        CacheZipUtils.init(applicationContext)
        IMG.init(applicationContext)
        if (TxtUtils.isEmpty(AppsConfig.FLAVOR)) {
            throw RuntimeException("Application not configured correctly!")
        }
        // Sudoku
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // channels
            val channel = NotificationChannel(ApplicationClass.Companion.CHANNEL_ID_SUDOKU, "Default", NotificationManager.IMPORTANCE_LOW)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }


    }
    override fun onTrimMemory(level: Int) {
        XEDMainActivity.withContext {
            binding?.viewpager2?.offscreenPageLimit = 1
        }
        ExtensionManager.onLowMemory()
        super.onTrimMemory(level)
        LOG.d("onTrimMemory", level)
    }
    override fun onLowMemory() {
        super.onLowMemory()
        LOG.d("AppState save onLowMemory")
        IMG.clearMemoryCache()
        TintUtil.clean()
        HypenUtils.cache.clear()
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //MultiDex.install(this)
    }
}