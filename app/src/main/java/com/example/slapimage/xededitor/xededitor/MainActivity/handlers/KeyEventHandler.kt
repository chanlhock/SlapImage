package com.example.slapimage.xededitor.xededitor.MainActivity.handlers

import android.view.KeyEvent
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.example.slapimage.xededitor.libcommons.DefaultScope
import com.example.slapimage.xededitor.libcommons.Printer
import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity
import com.example.slapimage.xededitor.xededitor.MainActivity.tabs.editor.EditorFragment
import com.example.slapimage.xededitor.xededitor.MainActivity.file.FileAction.Companion.to_save_file
import com.example.slapimage.xededitor.xededitor.MainActivity.tabs.editor.getCurrentEditorFragment
import com.example.slapimage.R
import io.github.rosemoe.sora.interfaces.KeyEventHandler
import io.github.rosemoe.sora.widget.EditorKeyEventHandler
import kotlinx.coroutines.launch

object KeyEventHandler {

    init {
        EditorKeyEventHandler.userKeyEventHandler =
            KeyEventHandler { isProcessedByEditor, editor, event, editorKeyEvent, keybindingEvent, keyCode, isShiftPressed, isAltPressed, isCtrlPressed ->
                if (event != null) {
                    onAppKeyEvent(event)
                }
                isProcessedByEditor
            }
    }

    private var lastCallTime = 0L
    fun onAppKeyEvent(keyEvent: KeyEvent) {

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastCallTime >= 100) {
            lastCallTime = currentTime
        }else{
            return
        }

        val currentFragment = getCurrentEditorFragment()
        val editor = getCurrentEditorFragment()?.editor

        if (keyEvent.isCtrlPressed) {
            when (keyEvent.keyCode) {
                KeyEvent.KEYCODE_W -> {
                    XEDMainActivity.activityRef.get()?.apply {
                        adapter!!.removeFragment(tabLayout!!.selectedTabPosition,true)
                        binding!!.tabs.invalidate()
                        binding!!.tabs.requestLayout()
                        
                        // Detach and re-attach the TabLayoutMediator
                        TabLayoutMediator(binding!!.tabs, viewPager!!) { tab, position ->
                            tab.text = tabViewModel.fragmentTitles[position]
                        }.attach()
                        DefaultScope.launch { updateMenu(XEDMainActivity.activityRef.get()?.adapter?.getCurrentFragment()) }
                    }
                }
                
                KeyEvent.KEYCODE_K -> {
                    XEDMainActivity.activityRef.get()?.let {
                        if (it.tabLayout!!.selectedTabPosition == 0) {
                            return
                        }
                        it.tabLayout!!.selectTab(
                            it.tabLayout!!.getTabAt(it.tabLayout!!.selectedTabPosition - 1)
                        )
                        val fragment = it.adapter!!.getCurrentFragment()?.fragment
                        if (fragment is EditorFragment) {
                            fragment.editor?.requestFocus()
                            fragment.editor?.requestFocusFromTouch()
                        }
                    }
                }
                
                KeyEvent.KEYCODE_L -> {
                    XEDMainActivity.activityRef.get()?.let {
                        if (it.tabLayout!!.selectedTabPosition == it.tabLayout!!.tabCount - 1) {
                            return
                        }
                        it.tabLayout!!.selectTab(
                            it.tabLayout!!.getTabAt(it.tabLayout!!.selectedTabPosition + 1)
                        )
                        val fragment = it.adapter!!.getCurrentFragment()?.fragment
                        if (fragment is EditorFragment) {
                            fragment.editor?.requestFocus()
                            fragment.editor?.requestFocusFromTouch()
                        }
                    }
                }
                
                KeyEvent.KEYCODE_S -> {
                    if (keyEvent.isShiftPressed) {
                        when (keyEvent.keyCode) {
                            KeyEvent.KEYCODE_S -> {
                                currentFragment?.getFile()?.let {
                                    to_save_file = it
                                    XEDMainActivity.activityRef.get()?.fileManager?.requestOpenDirectoryToSaveFile()
                                }
                            }
                        }
                    } else{
                        if (currentFragment is EditorFragment) {
                            currentFragment.save(false)
                        }
                    }
                }
                
                KeyEvent.KEYCODE_PLUS, 70 -> {
                    editor?.let {
                        if (it.textSizePx < 57) {
                            it.textSizePx += 2
                        }
                    }
                }
                
                KeyEvent.KEYCODE_MINUS -> {
                    editor?.let {
                        if (it.textSizePx > 8) {
                            it.textSizePx -= 2
                        }
                    }
                }
                
                KeyEvent.KEYCODE_F -> {
                    XEDMainActivity.activityRef.get()?.let {
                        it.lifecycleScope.launch {
                            MenuClickHandler.handle(it, it.menu!!.findItem(R.id.search))
                        }
                    }
                }
                
                KeyEvent.KEYCODE_P -> {
                    XEDMainActivity.activityRef.get()?.let {
                        if (currentFragment is EditorFragment){
                            val printer = Printer(it)
                            printer.setCodeText(editor?.text.toString(), language = currentFragment.file?.getName()?.substringAfterLast(".")?.trim() ?: "txt")
                        }
                    }
                }
                
                KeyEvent.KEYCODE_G -> {
                    editor?.let {
                        val line = 0
                        var column = (editor.text.getLine(line).length).coerceAtMost(
                            editor.cursor.leftColumn
                        )
                        if (column < 0) {
                            column = 0
                        }
                        editor.cursor.set(line, column)
                    }
                    
                }
                
                KeyEvent.KEYCODE_B -> {
                    editor?.let {
                        val line = editor.text.lineCount - 1
                        var column = (editor.text.getLine(line).length - 1).coerceAtMost(
                            editor.cursor.leftColumn
                        )
                        if (column < 0) {
                            column = 0
                        }
                        editor.cursor.set(line, column)
                    }
                    
                }
            }
            
        }
    }
}
