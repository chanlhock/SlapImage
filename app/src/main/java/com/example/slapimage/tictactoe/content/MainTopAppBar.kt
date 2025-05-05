package com.example.slapimage.tictactoe.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Help
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slapimage.R
import com.example.slapimage.tictactoe.ui.composables.AnimatedAppIcon
import com.example.slapimage.tictactoe.ui.composables.ClickableIcon
import com.example.slapimage.tictactoe.ui.composables.PersianText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsIconClick: () -> Unit,
    onAboutIconClick: () -> Unit
) {
    val appName = stringResource(R.string.app_name)
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = { AnimatedAppIcon() },
        actions = {
            SettingsIcon(onSettingsIconClick)
            AboutIcon(onAboutIconClick)
        },
        title = {
            PersianText(
                text = appName,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Composable
private fun AboutIcon(
    onAboutIconClick: () -> Unit
) {
    ClickableIcon(
        imageVector = Icons.AutoMirrored.TwoTone.Help,
        contentDescription = stringResource(R.string.about),
        onClick = onAboutIconClick
    )
}

@Composable
private fun SettingsIcon(
    onSettingsIconClick: () -> Unit
) {
    ClickableIcon(
        imageVector = Icons.TwoTone.Settings,
        contentDescription = stringResource(R.string.settings),
        onClick = onSettingsIconClick
    )
}

@Composable
private fun NavigationIcon(
    appName: String
) {
    Icon(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = appName,
        tint = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .padding(8.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground)
    )
}