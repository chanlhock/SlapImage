package com.example.slapimage.xededitor.xededitor.MainActivity.tabs.editor

import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity
import com.example.slapimage.xededitor.xededitor.MainActivity.tabs.core.CoreFragment

fun fragmentsForEach(callback:(CoreFragment)-> Unit){
    XEDMainActivity.activityRef.get()?.adapter?.tabFragments?.values?.forEach { weakRef ->
        weakRef.get()?.fragment?.let {
            callback.invoke(it)
        }
    }
}

fun editorFragmentsForEach(callback: (EditorFragment) -> Unit){
    fragmentsForEach { fragment ->
        if (fragment.isEditorFragment()){
            callback.invoke(fragment as EditorFragment)
        }
    }
}

fun CoreFragment.isEditorFragment(): Boolean{
    return this is EditorFragment
}

fun saveAllFiles(){
    editorFragmentsForEach {
        it.save(isAutoSaver = true)
    }
}

fun getCurrentFragment(): CoreFragment?{
    return XEDMainActivity.activityRef.get()?.adapter?.getCurrentFragment()?.fragment
}

fun getCurrentEditorFragment(): EditorFragment?{
    return getCurrentFragment() as? EditorFragment
}
