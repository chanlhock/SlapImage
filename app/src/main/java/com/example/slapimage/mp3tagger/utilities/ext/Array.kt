package com.example.slapimage.mp3tagger.utilities.ext

fun Array<String>?.joinToStringOrNull(separator: String = ", "): String? {
    return this?.joinToString(separator = separator)
}