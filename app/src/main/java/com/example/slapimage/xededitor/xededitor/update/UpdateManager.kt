package com.example.slapimage.xededitor.xededitor.update

import androidx.core.content.pm.PackageInfoCompat
import com.example.slapimage.xededitor.libcommons.application
import com.example.slapimage.xededitor.libcommons.child
import com.example.slapimage.xededitor.libcommons.localBinDir
import com.example.slapimage.xededitor.libcommons.toast
import com.example.slapimage.xededitor.resources.strings
import com.example.slapimage.xededitor.settings.Preference
import com.example.slapimage.xededitor.settings.Settings

object UpdateManager {
    private fun deleteCommonFiles() = with(application!!){
        codeCacheDir.apply {
            if (exists()){
                deleteRecursively()
            }
        }

        localBinDir().apply {
            if (exists()){
                deleteRecursively()
            }
        }
    }

    fun inspect() = with(application!!){
        val lastVersionCode = Settings.lastVersionCode
        val currentVersionCode = PackageInfoCompat.getLongVersionCode(packageManager.getPackageInfo(packageName, 0))

        if (lastVersionCode != currentVersionCode){
            //app is updated
            when(lastVersionCode){
                //what to do if the last version code matches this

                -1L -> {
                    deleteCommonFiles()
                }
                40L -> {
                    Preference.clearData()
                    deleteCommonFiles()
                    toast(strings.update_files_cleared)
                }
                48L -> {
                    deleteCommonFiles()
                }
                else -> {
                    deleteCommonFiles()
                }
            }

        }

        Settings.lastVersionCode = currentVersionCode

    }
}
