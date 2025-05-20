package com.example.slapimage.xededitor.xededitor.MainActivity.tabs.core

import android.view.View
import com.example.slapimage.xededitor.file_wrapper.FileObject

interface CoreFragment {
    fun getView(): View?
    fun onDestroy()
    fun onCreate()
    fun loadFile(file: FileObject)
    fun getFile(): FileObject?
    fun onClosed()
}