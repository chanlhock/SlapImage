// SPDX-License-Identifier: GPL-3.0-or-later

package com.example.slapimage.mbcompass.utils

object ToDegree {

    fun toDegree(azimuth: Float): Float {
        return (Math.toDegrees(azimuth.toDouble()).toFloat() + 360) % 360
    }

}