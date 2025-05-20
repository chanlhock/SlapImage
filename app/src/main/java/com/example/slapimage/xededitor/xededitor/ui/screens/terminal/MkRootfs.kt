package com.example.slapimage.xededitor.xededitor.ui.screens.terminal

import android.content.Context
import com.example.slapimage.xededitor.libcommons.alpineDir
import com.example.slapimage.xededitor.libcommons.alpineHomeDir
import com.example.slapimage.xededitor.libcommons.child
import com.example.slapimage.xededitor.libcommons.createFileIfNot
import com.example.slapimage.xededitor.libcommons.isMainThread
import com.example.slapimage.xededitor.resources.getString
import com.example.slapimage.xededitor.resources.strings
import com.example.slapimage.xededitor.xededitor.App.Companion.getTempDir
import java.io.File
import java.lang.Runtime.getRuntime

class MkRootfs(val context: Context, private val onComplete:()->Unit) {
    private val alpine = File(getTempDir(),"alpine.tar.gz")

    init {
        val rootfsFiles = alpineDir().listFiles()?.filter { 
    it.absolutePath != alpineHomeDir().absolutePath && it.absolutePath != alpineDir().child("tmp").absolutePath 
} ?: emptyList()


        if (alpine.exists().not() || rootfsFiles.isEmpty().not()){
            onComplete.invoke()
            println("completed")
        }else{
            initializeInternal()
        }
    }

    private fun initializeInternal(){
        if (isMainThread()){
            throw RuntimeException("IO operation on the main thread")
        }
        getRuntime().exec("tar -xf ${alpine.absolutePath} -C ${alpineDir()}").waitFor()
        alpine.delete()
        with(alpineDir()){
            child("etc/hostname").writeText("SlapOmage")   // Xed-Editor
            child("etc/resolv.conf").also { it.createFileIfNot();it.writeText(nameserver) }
            child("etc/hosts").writeText(hosts)
        }
        onComplete.invoke()
    }
}