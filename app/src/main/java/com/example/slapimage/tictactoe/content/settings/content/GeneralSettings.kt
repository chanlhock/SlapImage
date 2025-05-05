package com.example.slapimage.tictactoe.content.settings.content

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DisplaySettings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.slapimage.R
import com.example.slapimage.tictactoe.content.settings.SettingsItem
import com.example.slapimage.tictactoe.content.settings.SettingsItemCard
import com.example.slapimage.tictactoe.content.settings.ThemeSetting
import com.example.slapimage.tictactoe.ui.composables.PersianText
import com.example.slapimage.tictactoe.ui.composables.SingleLinePersianText
import com.example.slapimage.tictactoe.ui.composables.SwitchWithText

@Composable
internal fun ThemeChangerCard(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit
) {
    var isShowingThemeDialog by remember { mutableStateOf(false) }

    if (isShowingThemeDialog) {
        ThemeChangerDialog(
            currentTheme = currentTheme,
            onCurrentThemeChange = onCurrentThemeChange,
            onDismiss = { isShowingThemeDialog = false }
        )
    }

    SettingsItemCard(
        title = stringResource(R.string.theme),
        content = {
            SettingsItem(
                onClick = { isShowingThemeDialog = true },
                content = {
                    Icon(
                        imageVector = Icons.TwoTone.DisplaySettings,
                        contentDescription = stringResource(R.string.theme)
                    )
                    SingleLinePersianText(stringResource(currentTheme.persianNameStringResource))
                }
            )
            if (currentTheme == ThemeSetting.System && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                DynamicThemeNotice()
        }
    )
}

@Composable
private fun ThemeChangerDialog(
    currentTheme: ThemeSetting,
    onCurrentThemeChange: (ThemeSetting) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = remember { ThemeSetting.entries.toTypedArray() }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { /*ignored*/ },
        title = { PersianText(stringResource(R.string.theme)) },
        icon = { Icon(imageVector = Icons.TwoTone.DisplaySettings, contentDescription = null) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .selectableGroup()
                    .fillMaxWidth(),
                content = {
                    themes.forEach { theme ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (theme == currentTheme),
                                    role = Role.RadioButton,
                                    onClick = {
                                        onCurrentThemeChange(theme)
                                        onDismiss()
                                    }
                                ),
                            content = {
                                RadioButton(
                                    selected = (theme == currentTheme),
                                    onClick = null,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                                PersianText(
                                    text = stringResource(theme.persianNameStringResource),
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        )
                    }
                }
            )
        }
    )
}

@Composable
internal fun EffectsCard(
    isSoundOn: Boolean,
    isSoundOnChange: (Boolean) -> Unit,
    isVibrationOn: Boolean,
    isVibrationOnChange: (Boolean) -> Unit
) {
    SettingsItemCard(
        title = stringResource(R.string.effects),
        content = {
            SwitchWithText(
                caption = stringResource(R.string.sound_effects),
                checked = isSoundOn,
                onCheckedChange = isSoundOnChange
            )
            SwitchWithText(
                caption = stringResource(R.string.haptic_feedback),
                checked = isVibrationOn,
                onCheckedChange = isVibrationOnChange
            )
        }
    )
}

@Composable
private fun DynamicThemeNotice() {
    PersianText(
        text = stringResource(R.string.dynamic_theme_notice),
        textAlign = TextAlign.Justify
    )
}
