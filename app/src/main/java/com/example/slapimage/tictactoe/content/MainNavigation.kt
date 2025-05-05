package com.example.slapimage.tictactoe.content

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.slapimage.tictactoe.content.game.GameContent
import com.example.slapimage.tictactoe.content.settings.ThemeSetting
import com.example.slapimage.tictactoe.content.settings.content.SettingsContent
import com.example.slapimage.tictactoe.ui.navigation.Nav
import com.example.slapimage.tictactoe.ui.theme.DoozTheme
import com.example.slapimage.tictactoe.util.Constants
import com.example.slapimage.tictactoe.util.DataStoreHelper

@Composable
internal fun MainNavigation() {
    val context = LocalContext.current
    var theme by remember { mutableStateOf(ThemeSetting.System) }
    val dataStore = DataStoreHelper(context.settings)

    LaunchedEffect(Unit) {
        val savedTheme = dataStore.getString(Constants.theme)
        theme = try {
            ThemeSetting.valueOf(savedTheme ?: ThemeSetting.System.name)
        } catch (e: IllegalArgumentException) {
            ThemeSetting.System // Fallback to default if invalid value is stored
        }
    }
    //LaunchedEffect(Unit) {
    //    theme = ThemeSetting.valueOf(
    //        dataStore.getString(Constants.theme) ?: ThemeSetting.System.name
    //    )
   // }
    DoozTheme(
        isDarkTheme = isDarkTheme(theme, isSystemInDarkTheme()),
        isDynamicColor = theme == ThemeSetting.System,
        content = {
            Column {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.weight(1f),
                    navController = navController,
                    startDestination = Nav.Routes.game,
                    builder = {
                        composable(Nav.Routes.game) {
                            GameContent(
                                onNavigateToSettings = { navController.navigate(Nav.Routes.settings) },
                                onNavigateToAbout = { navController.navigate(Nav.Routes.about) }
                            )
                        }

                        composable(Nav.Routes.settings) {
                            SettingsContent(
                                onThemeChanged = { newTheme -> theme = newTheme },
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(Nav.Routes.about) { AboutContent(onBackClick = { navController.popBackStack() }) }
                    }
                )
            }
        }
    )
}

private fun isDarkTheme(
    themeSetting: ThemeSetting,
    isSystemInDarkTheme: Boolean
) = when (themeSetting) {
    ThemeSetting.Light -> false
    ThemeSetting.System -> isSystemInDarkTheme
    else -> themeSetting == ThemeSetting.Dark
}