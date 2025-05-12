package com.example.slapimage.mp3tagger.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.example.slapimage.mp3tagger.core.presentation.common.Route
import com.example.slapimage.mp3tagger.core.util.getNeededStoragePermissions
import com.example.slapimage.mp3tagger.onboarding.presentation.pages.OnboardingPermissionsPage
import com.example.slapimage.mp3tagger.onboarding.presentation.pages.OnboardingWelcomePage
import com.example.slapimage.mp3tagger.ui.motion.animatedComposable
import com.example.slapimage.mp3tagger.utilities.ui.permission.PermissionType.Companion.toPermissionType

fun NavGraphBuilder.onboardingRouting(
    onNavigate: (Route) -> Unit,
    onCompletedOnboarding: () -> Unit
) {
    navigation<Route.OnboardingNavigator>(
        startDestination = Route.OnboardingNavigator.Welcome::class,
    ) {
        animatedComposable<Route.OnboardingNavigator.Welcome> {
            OnboardingWelcomePage(
                onGetStarted = {
                    onNavigate(Route.OnboardingNavigator.Permissions)
                }
            )
        }

        animatedComposable<Route.OnboardingNavigator.Permissions> {

            val neededPermissions by remember { mutableStateOf(getNeededStoragePermissions().map { it.toPermissionType() }) }

            OnboardingPermissionsPage(
                neededPermissions = neededPermissions,
                onNextClick = {
                    onCompletedOnboarding()
                }
            )
        }
    }
}