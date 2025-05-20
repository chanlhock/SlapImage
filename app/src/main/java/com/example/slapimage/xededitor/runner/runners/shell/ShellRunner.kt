package com.example.slapimage.xededitor.runner.runners.shell

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.slapimage.xededitor.karbon_exec.launchInternalTerminal
import com.example.slapimage.xededitor.karbon_exec.runBashScript
import com.example.slapimage.xededitor.libcommons.TerminalCommand
import com.example.slapimage.xededitor.resources.drawables
import com.example.slapimage.xededitor.runner.RunnerImpl
import com.example.slapimage.xededitor.settings.Settings
import java.io.File

class ShellRunner(val file: File,val isTermuxFile: Boolean = false) : RunnerImpl() {
    override fun run(context: Context) {
        val runtime = if (isTermuxFile){"Termux"}else{
            Settings.terminal_runtime
        }

        when(runtime){
            "Android" -> {
                launchInternalTerminal(
                    context = context,
                    TerminalCommand(
                        shell = "/system/bin/sh",
                        args = arrayOf("-c",file.absolutePath),
                        id = "shell-android",
                        alpine = false,
                        workingDir = file.parentFile!!.absolutePath
                    )
                )
            }
            "Alpine" -> {
                launchInternalTerminal(
                    context = context,
                    TerminalCommand(
                        shell = "/bin/sh",
                        args = arrayOf(file.absolutePath),
                        id = "shell",
                        alpine = true,
                        workingDir = file.parentFile!!.absolutePath
                    )
                )
            }
            "Termux" -> {
                runBashScript(context, workingDir = file.parentFile!!.absolutePath, script = """
                    bash ${file.absolutePath}
                    echo -e "\n\nProcess completed. Press Enter to go back to Xed-Editor."
                    read
                """.trimIndent())
            }
        }
    }

    override fun getName(): String {
        return Settings.terminal_runtime
    }

    override fun getDescription(): String {
        return Settings.terminal_runtime
    }

    override fun getIcon(context: Context): Drawable? {
        return ContextCompat.getDrawable(
            context,
            drawables.bash,
        )
    }

    override fun isRunning(): Boolean {
        return false
    }

    override fun stop() {

    }
}
