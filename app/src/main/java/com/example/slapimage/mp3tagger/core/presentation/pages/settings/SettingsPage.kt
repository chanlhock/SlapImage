package com.example.slapimage.mp3tagger.core.presentation.pages.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.slapimage.R
import com.example.slapimage.mp3tagger.core.presentation.common.LocalNavController
import com.example.slapimage.mp3tagger.core.presentation.common.Route
import com.example.slapimage.mp3tagger.ui.components.preferences.SettingsGroup
import com.example.slapimage.mp3tagger.ui.components.preferences.SettingsItem
import com.example.slapimage.mp3tagger.ui.components.topbar.ColumnWithCollapsibleTopBar

@OptIn(ExperimentalMaterial3Api::class) //, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsPage(
    onBackPressed: () -> Unit
) {
    val navController = LocalNavController.current
    var collapseFraction by remember { mutableFloatStateOf(0f) }

    val mainSettingsGroup: List<SettingsItem> = listOf(
        SettingsItem(
            title = stringResource(id = R.string.general),
            supportingText = stringResource(id = R.string.general_description),
            icon = Icons.Rounded.Settings,
            onClick = {
                navController.navigate(Route.SettingsNavigator.Settings.General)
            }),
    )

    ColumnWithCollapsibleTopBar(
        topBarContent = {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack, // .ArrowBackIosNew,
                    contentDescription = stringResource(id = R.string.back)
                )
            }

            Text(
                text = stringResource(id = R.string.settings),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
                    .graphicsLayer {
                        val scale = lerp(0.7f, 1f, collapseFraction)
                        scaleX = scale
                        scaleY = scale
                    }
            )
        },
        collapseFraction = {
            collapseFraction = it
        },
        contentPadding = PaddingValues(horizontal = 32.dp),
        contentHorizontalAlignment = Alignment.CenterHorizontally,
        contentVerticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        SettingsGroup(
            items = mainSettingsGroup,
            modifier = Modifier
        )

    }
}