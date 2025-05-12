package com.example.slapimage.mp3tagger.core.ext

import kotlin.reflect.KClass

fun KClass<*>.qualifiedName(): String = this.qualifiedName.toString()