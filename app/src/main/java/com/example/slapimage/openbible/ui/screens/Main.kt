package com.example.slapimage.openbible.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.slapimage.openbible.logic.checkForUpdates
import com.example.slapimage.openbible.logic.fixLegacy
import com.example.slapimage.openbible.logic.getCheckAtStartup
import com.example.slapimage.openbible.logic.getIndex
import com.example.slapimage.openbible.logic.getTranslationList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.coroutines.coroutineScope

@Serializable
object Bookmarks

@Serializable
object Read

@Serializable
object Search

@Serializable
data class Selection(val isSplitScreen: Boolean)

@Serializable
object Settings

@Serializable
object Start

@Composable
fun App(onThemeChange: (Boolean?, Boolean?, Boolean?) -> Unit) {
    val context = LocalContext.current
    fixLegacy(context)
    val startDestination: Any =
        if (!getIndex(context).exists() ||
            getTranslationList(context).isEmpty() ||
            (getCheckAtStartup(context) && checkForUpdates(context, false))
        ) Start else Read

    val navController = rememberNavController()
    NavHost(navController, startDestination = startDestination) {
      /*  composable<Bookmarks> {
            BookmarksScreen(onNavigateToRead = {
                navController.navigate(Read) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }*/
        composable<Read> {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            ReadScreen(
                //onNavigateToBookmarks = { navController.navigate(Bookmarks) },
                //onNavigateToRead = {
               //     navController.navigate(Read) {
                //        popUpTo(0) { inclusive = true }
               //     }
               // },
                onNavigateToRead = {
                    coroutineScope.launch(Dispatchers.Main.immediate) {
                        // Optional pre-navigation work
                        withFrameNanos { } // Yield to next frame

                        navController.navigate(Read) {      //navigate
                            anim {
                                enter = android.R.anim.fade_in
                                exit = android.R.anim.fade_out
                                popEnter = android.R.anim.fade_in
                                popExit = android.R.anim.fade_out
                            }
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onNavigateToSearch = { navController.navigate(Search) },
                onNavigateToSelection = { isSplitScreen ->
                    navController.navigate(Selection(isSplitScreen))
                },
                onNavigateToSettings = { navController.navigate(Settings) },
                onNavigateToStart = {
                    navController.navigate(Start) {
                        popUpTo(0) { inclusive = true }
                    }
                })
        }
        composable<Search> {
            SearchScreen(onNavigateToRead = {
                navController.navigate(Read) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        composable<Selection> { backStackEntry ->
            val selection = backStackEntry.toRoute<Selection>()
            SelectionScreen(
                onNavigateToRead = {
                    navController.navigate(Read) {
                        popUpTo(0) { inclusive = true }
                    }
                }, selection.isSplitScreen
            )
        }
        composable<Settings> {
            val coroutineScope = rememberCoroutineScope()
            SettingsScreen(
                onNavigateToRead = {
                    coroutineScope.launch {
                        navController.navigate(Read) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }, onThemeChange = onThemeChange
            )
        }
        composable<Start> {
            StartScreen(onNavigateToRead = {
                navController.navigate(Read) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
    }
}