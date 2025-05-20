package com.example.slapimage.xededitor.runner.runners.node

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.slapimage.xededitor.file_wrapper.FileObject
import com.example.slapimage.xededitor.file_wrapper.FileWrapper
import com.example.slapimage.xededitor.karbon_exec.launchInternalTerminal
import com.example.slapimage.xededitor.karbon_exec.runBashScript
import com.example.slapimage.xededitor.libcommons.TerminalCommand
import com.example.slapimage.xededitor.libcommons.child
import com.example.slapimage.xededitor.libcommons.localBinDir
import com.example.slapimage.xededitor.resources.drawables
import com.example.slapimage.xededitor.runner.RunnerImpl
import com.example.slapimage.xededitor.settings.Settings
import java.io.File

class NodeRunner(val file:File,val isTermuxFile: Boolean = false) : RunnerImpl() {
    override fun run(context: Context) {
        val node = localBinDir().child("node")
        if (node.exists().not()) {
            node.writeText(context.assets.open("terminal/nodejs.sh").bufferedReader()
                .use { it.readText() })
        }
        val runtime = if (isTermuxFile){"Termux"}else{
            Settings.terminal_runtime
        }
        when(runtime){
            "Alpine","Android" -> {
                launchInternalTerminal(
                    context = context, TerminalCommand(
                        shell = "/bin/sh",
                        args = arrayOf(node.absolutePath,file.absolutePath),
                        id = "node",
                        workingDir = file.parentFile!!.absolutePath
                    )
                )
            }
            "Termux" -> {
                runBashScript(
                    context,
                    workingDir = file.parentFile!!.absolutePath,
                    script = """
    required_packages="nodejs"
    missing_packages=""

    # Check for missing packages
    for pkg in ${'$'}required_packages; do
        if ! dpkg -l | grep -q "^ii  ${'$'}pkg"; then
            missing_packages="${'$'}missing_packages ${'$'}pkg"
        fi
    done

    # Install missing packages if any
    if [ -n "${'$'}missing_packages" ]; then
        echo -e "\e[34;1m[*]\e[37m Installing missing packages: ${'$'}missing_packages\e[0m"
        pkg install -y ${'$'}missing_packages
    fi

    node "${file.absolutePath}"
    echo -e "\n\nProcess completed. Press Enter to go back to Xed-Editor."
    read
""".trimIndent()
                )
            }
        }
    }

    override fun getName(): String {
        return "Nodejs"
    }

    override fun getDescription(): String {
        return "Nodejs"
    }

    override fun getIcon(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, drawables.ic_language_js)
    }

    override fun isRunning(): Boolean {
        return false
    }

    override fun stop() {

    }
}
