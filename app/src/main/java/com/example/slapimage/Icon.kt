// Icon.kt
package com.example.slapimage

data class Icon(
    val image: Int,      // Drawable resource ID
    val text: String,     // Label text
    val cacheKey: String = "$image-$text" // Unique key for caching
)