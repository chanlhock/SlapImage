package com.example.slapimage.mp3tagger.features.spotify.domain.services

import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.models.Token

interface SpotifyService {
    suspend fun getSpotifyApi(): SpotifyAppApi
    suspend fun getSpotifyToken(): Token
}