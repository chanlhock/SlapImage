package com.example.slapimage.xededitor.xededitor.ui.screens.terminal

import com.example.slapimage.xededitor.libcommons.alpineDir
import com.example.slapimage.xededitor.libcommons.application
import com.example.slapimage.xededitor.libcommons.child
import com.example.slapimage.xededitor.libcommons.createFileIfNot
import com.example.slapimage.xededitor.libcommons.localBinDir
import com.example.slapimage.xededitor.libcommons.localDir

fun setupTerminalFiles(){

    with(localBinDir().child("init-host")){
        if (exists().not()) {
            createFileIfNot()
            writeText(
                application!!.assets.open("terminal/init-host.sh").bufferedReader()
                    .use { it.readText() })
        }
    }

    with(localBinDir().child("init")){
        if (exists().not()) {
            createFileIfNot()
            writeText(
                application!!.assets.open("terminal/init.sh").bufferedReader()
                    .use { it.readText() })
        }
    }

    with(alpineDir().child("bin/logger")){
        if (exists().not()) {
            createFileIfNot()
            setExecutable(true)
            writeText(
                application!!.assets.open("terminal/log.sh").bufferedReader()
                    .use { it.readText() })
        }
    }


    with(localDir().child("stat")){
        if (exists().not()){
            createFileIfNot()
            writeText(stat)
        }
    }

    with(localDir().child("vmstat")){
        if (exists().not()){
            createFileIfNot()
            writeText(vmstat)
        }
    }


}