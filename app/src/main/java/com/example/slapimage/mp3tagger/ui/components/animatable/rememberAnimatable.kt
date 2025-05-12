package com.example.slapimage.mp3tagger.ui.components.animatable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.slapimage.mp3tagger.ui.util.AnimatableSaver

@Composable
fun rememberAnimatable(
    initialValue: Float,
    visibilityThreshold: Float = Spring.DefaultDisplacementThreshold
): Animatable<Float, AnimationVector1D> {
    return rememberSaveable(
        saver = AnimatableSaver
    ) {
        Animatable(initialValue, visibilityThreshold)
    }
}