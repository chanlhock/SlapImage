package com.example.slapimage.tictactoe.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.slapimage.R
import com.example.slapimage.tictactoe.ui.composables.AnimatedAppIcon
import com.example.slapimage.tictactoe.ui.composables.ClickableIcon
import com.example.slapimage.tictactoe.ui.composables.PersianText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSettingsIconClick: () -> Unit
) {
    val appName = stringResource(R.string.tictactoe)
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = { AnimatedAppIcon() },
        actions = {
            SettingsIcon(onSettingsIconClick)
        },
        title = {
            PersianText(
                text = appName,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF1E88E5), // Dark blue color
            titleContentColor = Color.White, // White text for contrast
            navigationIconContentColor = Color.White, // White icon
            actionIconContentColor = Color.White // White icon
        )

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

