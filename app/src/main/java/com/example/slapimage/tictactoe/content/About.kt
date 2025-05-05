package com.example.slapimage.tictactoe.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.slapimage.R
import com.example.slapimage.tictactoe.ui.composables.PersianText
import com.example.slapimage.tictactoe.ui.composables.Ripple
import com.example.slapimage.tictactoe.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AboutContent(
    onBackClick: () -> Unit
) {
    ScaffoldWithTitle(
        title = stringResource(R.string.about),
        onBackClick = onBackClick,
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        content = {
                            val uriHandler = LocalUriHandler.current
                            val sourceUri = stringResource(R.string.github_source)
                            val licenseUri = stringResource(R.string.license_link)
                            val developerUri = stringResource(R.string.developer_uri)
                            Ripple(
                                onClick = { uriHandler.openUri(licenseUri) },
                                content = {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_gplv3),
                                        contentDescription = stringResource(id = R.string.gplv3_image_description),
                                        modifier = Modifier
                                            .padding(32.dp)
                                            .fillMaxWidth(),
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                    )
                                }
                            )
                            PersianText(
                                text = stringResource(R.string.license_header),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Ripple(
                                onClick = { uriHandler.openUri(sourceUri) },
                                content = {
                                    Text(
                                        text = sourceUri,
                                        textDecoration = TextDecoration.Underline
                                    )
                                }
                            )
                            Ripple(
                                onClick = { uriHandler.openUri(developerUri) },
                                content = {
                                    Text(
                                        text = developerUri,
                                        textDecoration = TextDecoration.Underline
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}