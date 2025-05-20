package com.example.slapimage.xededitor.libcommons

import androidx.lifecycle.lifecycleScope
import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

//same as MainActivity.lifeCycleScope
@OptIn(DelicateCoroutinesApi::class)
val DefaultScope:CoroutineScope
    get() {
        return XEDMainActivity.activityRef.get()?.lifecycleScope ?: GlobalScope
    }
