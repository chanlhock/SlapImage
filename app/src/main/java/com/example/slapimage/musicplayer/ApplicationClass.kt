package com.example.slapimage.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
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

class ApplicationClass:Application() {
    companion object{
        const val CHANNEL_ID = "MusicNotification"
        const val PLAY = "play"
        const val NEXT = "next"
        const val PREVIOUS = "previous"
        const val EXIT = "exit"
    }
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
    }
}