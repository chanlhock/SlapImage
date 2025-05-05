package com.example.slapimage.tictactoe.content.settings

import android.content.Context
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowDropDownCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slapimage.tictactoe.ui.DefaultCornerShape
import com.example.slapimage.tictactoe.ui.composables.PersianText
import com.example.slapimage.tictactoe.ui.composables.SingleLinePersianText

@Composable
internal fun SettingsItemCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
        content = {
            PersianText(
                text = title,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Card(
                modifier = modifier,
                shape = DefaultCornerShape,
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = content
                    )
                }
            )
        }
    )
}

@Composable
internal fun SettingsItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = LocalIndication.current,
            onClick = onClick,
        ),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp),
                        content = content
                    )
                    Icon(
                        imageVector = Icons.TwoTone.ArrowDropDownCircle,
                        contentDescription = null
                    )
                }
            )
        }
    )
}

@Composable
internal fun <T> SettingsSelectorDialog(
    title: String,
    icon: (@Composable () -> Unit)? = null,
    displayProvider: (T, Context) -> String = { item, _ -> item.toString() },
    options: List<T>,
    currentItem: T,
    onDismiss: () -> Unit,
    onOptionChanged: (T) -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {/*ignored*/ },
        icon = icon,
        title = { SingleLinePersianText(title) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .selectableGroup()
                    .fillMaxWidth(),
                content = {
                    options.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (item == currentItem),
                                    role = Role.RadioButton,
                                    onClick = {
                                        onOptionChanged(item)
                                        onDismiss()
                                    }
                                ),
                            content = {
                                RadioButton(
                                    selected = (item == currentItem),
                                    onClick = null,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                                PersianText(
                                    text = displayProvider(item, context),
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
