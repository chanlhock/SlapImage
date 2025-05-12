package com.example.slapimage.mp3tagger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.example.slapimage.mp3tagger.core.data.local.preferences.PreferencesKey.COMPLETED_ONBOARDING
import com.example.slapimage.mp3tagger.core.data.local.preferences.UserPreferences
import com.example.slapimage.mp3tagger.core.data.local.preferences.datastore.rememberPreferenceState
import com.example.slapimage.mp3tagger.core.presentation.common.Route
import com.example.slapimage.mp3tagger.core.presentation.settingsRouting
import com.example.slapimage.mp3tagger.core.util.cleanNavigate
import com.example.slapimage.mp3tagger.core.util.navigateBack
//import com.example.slapimage.mp3tagger.mediaplayer.mediaplayerRouting
//import com.example.slapimage.mp3tagger.mediaplayer.presentation.pages.mediaplayer.MediaplayerViewModel
import com.example.slapimage.mp3tagger.mediastore.presentation.MediaStorePageViewModel
import com.example.slapimage.mp3tagger.mediastore.presentation.pages.home.HomePage
import com.example.slapimage.mp3tagger.onboarding.onboardingRouting
import com.example.slapimage.mp3tagger.tageditor.tagEditorRouting
import com.example.slapimage.mp3tagger.ui.motion.animatedComposable
import com.example.slapimage.mp3tagger.ui.util.recomposeHighlighter
import org.koin.androidx.compose.koinViewModel
import com.example.slapimage.mp3tagger.utilities.mediastore.model.Song
import com.example.slapimage.mp3tagger.utilities.states.ResourceState
//@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigator(
    navController: NavHostController,
    startDestination: Route,
    preferences: State<UserPreferences>,
) {
    val mediaStoreViewModel = koinViewModel<MediaStorePageViewModel>()
    //val mediaplayerViewModel = koinViewModel<MediaplayerViewModel>()

    val (_, setOnboardingCompleted) = rememberPreferenceState(COMPLETED_ONBOARDING)

    NavHost(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        navController = navController,
        startDestination = startDestination,
    ) {
        onboardingRouting(
            onNavigate = { navController.navigate(it) },
            onCompletedOnboarding = {
                setOnboardingCompleted(true)
                navController.cleanNavigate(Route.MetadatorNavigator)
            }
        )

        navigation<Route.MetadatorNavigator>(
            startDestination = Route.MetadatorNavigator.Home,
        ) {
            animatedComposable<Route.MetadatorNavigator.Home> {
                val songsState =
                    mediaStoreViewModel.songs.collectAsStateWithLifecycle()
                HomePage(
                    songs = songsState,
                    preferences = preferences,
                    onEvent = mediaStoreViewModel::onEvent
                )
            }
        }

   //     mediaplayerRouting(
   //         mediaplayerViewModel = mediaplayerViewModel,
  //          onNavigateBack = {
  //              navController.navigateBack()
  //          }
  //      )

        tagEditorRouting { navController.navigateBack() }
        settingsRouting { navController.navigateBack() }
    }
}

