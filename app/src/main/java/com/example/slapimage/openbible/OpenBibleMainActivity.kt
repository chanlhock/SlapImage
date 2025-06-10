package com.example.slapimage.openbible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.slapimage.openbible.logic.getMainThemeOptions
import com.example.slapimage.openbible.ui.screens.App
import com.example.slapimage.openbible.ui.theme.OpenBibleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.runtime.LaunchedEffect

class OpenBibleMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }
}
/*
@Composable
fun MainApp() {
    val context = LocalContext.current
    val systemDarkTheme = isSystemInDarkTheme()
    var (darkTheme, dynamicColor, amoled) = getMainThemeOptions(context)

    if (darkTheme == null) darkTheme = systemDarkTheme

    val isDarkTheme = remember { mutableStateOf(darkTheme) }
    val isDynamicColor = remember { mutableStateOf(dynamicColor) }
    val isAmoled = remember { mutableStateOf(amoled) }

    OpenBibleTheme(
        darkTheme = isDarkTheme.value,
        dynamicColor = isDynamicColor.value,
        amoled = isAmoled.value
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            App(
                onThemeChange = { newDarkTheme, newDynamicColor, newAmoled ->
                    if (newDarkTheme != null) isDarkTheme.value =
                        newDarkTheme else isDarkTheme.value = systemDarkTheme
                    if (newDynamicColor != null) isDynamicColor.value = newDynamicColor
                    if (newAmoled != null) isAmoled.value = newAmoled
                }
            )
        }
    }
}
*/

@Composable
fun MainApp() {
    val context = LocalContext.current
    val systemDarkTheme = isSystemInDarkTheme()

    // Create individual mutable states with properly typed default values
    val isDarkTheme = remember { mutableStateOf(systemDarkTheme) }
    val isDynamicColor = remember { mutableStateOf(false) }
    val isAmoled = remember { mutableStateOf(false) }

    // Load theme options asynchronously
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val (dark, dynamic, amoled) = getMainThemeOptions(context)
            withContext(Dispatchers.Main) {
                isDarkTheme.value = dark ?: systemDarkTheme
                isDynamicColor.value = dynamic
                isAmoled.value = amoled
            }
        }
    }

    OpenBibleTheme(
        darkTheme = isDarkTheme.value,
        dynamicColor = isDynamicColor.value,
        amoled = isAmoled.value
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            App(
                onThemeChange = { newDarkTheme, newDynamicColor, newAmoled ->
                    isDarkTheme.value = newDarkTheme ?: systemDarkTheme
                    if (newDynamicColor != null) isDynamicColor.value = newDynamicColor
                    if (newAmoled != null) isAmoled.value = newAmoled
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainApp()
}