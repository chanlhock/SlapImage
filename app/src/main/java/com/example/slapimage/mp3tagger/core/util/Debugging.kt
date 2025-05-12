package com.example.slapimage.mp3tagger.core.util

import com.example.slapimage.BuildConfig

//execute the code inside if it is a debug release
fun executeIfDebugging(debugOnlyOperation: () -> Unit) {
    if (BuildConfig.DEBUG) debugOnlyOperation()
}