package com.example.slapimage.mp3tagger.core.domain.model

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.example.slapimage.mp3tagger.utilities.mediastore.model.UriSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Immutable
@Serializable
data class ParcelableSong(
    val name: String,
    val mainArtist: String,
    val localPath: String,
    @Serializable(with = UriSerializer::class) val artworkPath: Uri? = null,
    val filename: String
) : Parcelable