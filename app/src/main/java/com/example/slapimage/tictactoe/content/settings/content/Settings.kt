package com.example.slapimage.tictactoe.content.settings.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Games
import androidx.compose.material.icons.twotone.People
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.slapimage.R
import com.example.slapimage.tictactoe.content.settings.ThemeSetting
import com.example.slapimage.tictactoe.content.settings.rememberSettingsState
import com.example.slapimage.tictactoe.ui.composables.MySnackbar
import com.example.slapimage.tictactoe.ui.composables.PersianText
import com.example.slapimage.tictactoe.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onThemeChanged: (ThemeSetting) -> Unit,
    onBackClick: () -> Unit
) {
    val state = rememberSettingsState()
    ScaffoldWithTitle(
        title = stringResource(R.string.settings),
        onBackClick = onBackClick,
        snackbarHost = {
            SnackbarHost(state.snackbarHostState) { data ->
                MySnackbar {
                    PersianText(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = data.visuals.message
                    )
                }
            }
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = {
                    val tabIndex = rememberSaveable { mutableIntStateOf(1) }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        content = {
                            val tabTitles = listOf(
                                stringResource(R.string.general),
                                stringResource(R.string.game),
                                stringResource(R.string.players)
                            )

                            ScrollableTabRow(
                                selectedTabIndex = tabIndex.intValue,
                                tabs = {
                                    val icons = remember {
                                        listOf(
                                            Icons.TwoTone.Settings,
                                            Icons.TwoTone.Games,
                                            Icons.TwoTone.People
                                        )
                                    }

                                    tabTitles.forEachIndexed { index, title ->
                                        Tab(
                                            selected = tabIndex.intValue == index,
                                            onClick = { tabIndex.intValue = index },
                                            text = { PersianText(title) },
                                            icon = {
                                                Icon(
                                                    imageVector = icons.getOrNull(index)
                                                        ?: Icons.TwoTone.Settings,
                                                    contentDescription = title
                                                )
                                            }
                                        )
                                    }
                                }
                            )
                            when (tabTitles[tabIndex.intValue]) {
                                stringResource(R.string.general) -> {
                                    ThemeChangerCard(state.themeSetting) { newTheme ->
                                        state.themeSetting = newTheme
                                        onThemeChanged(newTheme)
                                    }
                                    EffectsCard(
                                        isSoundOn = state.isSoundOn,
                                        isSoundOnChange = { state.isSoundOn = it },
                                        isVibrationOn = state.isVibrationOn,
                                        isVibrationOnChange = { state.isVibrationOn = it }
                                    )
                                }

                                stringResource(R.string.game) -> {
                                    GeneralGameSettings(
                                        gamePlayersType = state.gamePlayersType,
                                        onPlayerTypeChange = { state.gamePlayersType = it },
                                        firstPlayerPolicy = state.firstPlayerPolicy,
                                        onFirstPlayerPolicyChange = { state.firstPlayerPolicy = it }
                                    )
                                    AiDifficultyCard(
                                        aiDifficulty = state.aiDifficulty,
                                        onDifficultyChanged = { state.aiDifficulty = it }
                                    )
                                    GameSizeChanger(
                                        gameSize = state.gameSize,
                                        onGameSizeIncrease = { state.gameSize++ },
                                        onGameSizeDecrease = { state.gameSize-- }
                                    )
                                }

                                stringResource(R.string.players) -> {
                                    PlayerCustomization(
                                        onSave = { state.saveNames() },
                                        firstPlayerName = state.firstPlayerName,
                                        onFirstPlayerNameChange = { state.firstPlayerName = it },
                                        secondPlayerName = state.secondPlayerName,
                                        onSecondPlayerNameChange = { state.secondPlayerName = it },
                                        firstPlayerShape = state.firstPlayerShape,
                                        onFirstPlayerShapeChange = { state.firstPlayerShape = it },
                                        secondPlayerShape = state.secondPlayerShape,
                                        onSecondPlayerShapeChange = { state.secondPlayerShape = it }
                                    )
                                }
                            }
                        }
                    )
                }
            )
        }
    )
}