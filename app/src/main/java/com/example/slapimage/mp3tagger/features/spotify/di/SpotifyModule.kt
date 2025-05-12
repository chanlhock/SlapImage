package com.example.slapimage.mp3tagger.features.spotify.di

import com.example.slapimage.BuildConfig
import com.example.slapimage.mp3tagger.features.spotify.data.remote.SpotifyServiceImpl
import com.example.slapimage.mp3tagger.features.spotify.data.remote.search.SpotifySearchServiceImpl
import com.example.slapimage.mp3tagger.features.spotify.domain.services.SpotifyService
import com.example.slapimage.mp3tagger.features.spotify.domain.services.search.SpotifySearchService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val spotifyMainModule = module {
    single(named("client_id")) { BuildConfig.CLIENT_ID }
    single(named("client_secret")) { BuildConfig.CLIENT_SECRET }
    single<SpotifyService> { SpotifyServiceImpl() }
}

val spotifyServicesModule = module {
    single<SpotifySearchService> { SpotifySearchServiceImpl(get()) }
}