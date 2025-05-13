package com.example.slapimage.mp3tagger.core.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.example.slapimage.mp3tagger.core.presentation.common.Route
import com.example.slapimage.mp3tagger.core.presentation.pages.settings.SettingsPage
import com.example.slapimage.mp3tagger.core.presentation.pages.settings.modules.GeneralSettingsPage
import com.example.slapimage.mp3tagger.ui.motion.animatedComposable

fun NavGraphBuilder.settingsRouting(
    onNavigateBack: () -> Unit
) {
    navigation<Route.SettingsNavigator>(
        startDestination = Route.SettingsNavigator.Settings,
    ) {
        animatedComposable<Route.SettingsNavigator.Settings> {
            SettingsPage(
                onBackPressed = onNavigateBack
            )
            GeneralSettingsPage()
        }

        animatedComposable<Route.SettingsNavigator.Settings.General> {
            GeneralSettingsPage()
        }

        animatedComposable<Route.SettingsNavigator.Settings.Appearance> {

        }

        animatedComposable<Route.SettingsNavigator.Settings.About> {

        }
    }
}