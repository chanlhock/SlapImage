
package com.example.slapimage.mp3tagger.ui.components.preferences

sealed class SettingOption(
    val title: String,
    val onSelection: () -> Unit,
)
