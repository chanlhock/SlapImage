package com.example.slapimage.mp3tagger.core.data.local.preferences

import androidx.compose.runtime.Stable
import com.example.slapimage.mp3tagger.core.data.local.DarkThemePreference
import com.example.slapimage.mp3tagger.core.data.local.preferences.PreferencesKey.MARQUEE_TEXT_ENABLED
import com.example.slapimage.mp3tagger.core.data.local.preferences.PreferencesKey.PALETTE_STYLE
import com.example.slapimage.mp3tagger.core.data.local.preferences.PreferencesKey.REDUCE_SHADOWS
import com.example.slapimage.mp3tagger.core.data.local.preferences.PreferencesKey.SONGS_LAYOUT
import com.example.slapimage.mp3tagger.core.data.local.preferences.PreferencesKey.SONG_CARD_SIZE
import com.example.slapimage.mp3tagger.core.data.local.preferences.PreferencesKey.THEME_COLOR
import com.example.slapimage.mp3tagger.core.data.local.preferences.PreferencesKey.USE_DYNAMIC_COLORING
import com.example.slapimage.mp3tagger.mediastore.domain.enums.LayoutType
import com.example.slapimage.mp3tagger.mediastore.domain.enums.CompactCardSize
import com.materialkolor.PaletteStyle

@Stable
data class UserPreferences(
    val songsLayout: LayoutType,
    val songCardSize: CompactCardSize,
    val reduceShadows: Boolean,
    val marqueeTextEnabled: Boolean,
    val darkThemePreference: DarkThemePreference,
    val useDynamicColoring: Boolean,
    val themeColor: Int,
    val paletteStyle: PaletteStyle
) {
    companion object {
        fun emptyUserPreferences(): UserPreferences =
            UserPreferences(
                songsLayout = LayoutType.valueOf(SONGS_LAYOUT.defaultValue),
                reduceShadows = REDUCE_SHADOWS.defaultValue,
                marqueeTextEnabled = MARQUEE_TEXT_ENABLED.defaultValue,
                songCardSize = CompactCardSize.valueOf(SONG_CARD_SIZE.defaultValue),
                darkThemePreference = DarkThemePreference(),
                useDynamicColoring = USE_DYNAMIC_COLORING.defaultValue,
                themeColor = THEME_COLOR.defaultValue,
                paletteStyle = PaletteStyle.valueOf(PALETTE_STYLE.defaultValue)
            )
    }
}
