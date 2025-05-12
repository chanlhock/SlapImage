package com.example.slapimage.mp3tagger.onboarding.domain

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.slapimage.mp3tagger.utilities.ui.permission.PermissionType

@Stable
data class PermissionItem(
    val permission: PermissionType,
    val icon: ImageVector,
    val isGranted: Boolean,
    val onClick: () -> Unit
)