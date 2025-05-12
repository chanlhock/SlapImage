package com.example.slapimage.mp3tagger.core.ext

import com.example.slapimage.mp3tagger.core.domain.model.ParcelableSong
import com.example.slapimage.mp3tagger.utilities.mediastore.model.Song

fun Song.toParcelableSong(): ParcelableSong {
    return ParcelableSong(
        name = this.title,
        mainArtist = this.artist,
        localPath = this.path,
        artworkPath = this.artworkPath,
        filename = this.fileName
    )
}