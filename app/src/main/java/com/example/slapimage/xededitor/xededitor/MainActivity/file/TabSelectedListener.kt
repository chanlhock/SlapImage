package com.example.slapimage.xededitor.xededitor.MainActivity.file

import androidx.appcompat.widget.PopupMenu
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.example.slapimage.xededitor.libcommons.DefaultScope
import com.example.slapimage.xededitor.libcommons.errorDialog
import com.example.slapimage.xededitor.resources.getString
import com.example.slapimage.xededitor.resources.strings
import com.example.slapimage.xededitor.settings.Settings
import com.example.slapimage.xededitor.xededitor.MainActivity.XEDMainActivity
import com.example.slapimage.xededitor.xededitor.MainActivity.currentTab
import com.example.slapimage.xededitor.xededitor.MainActivity.handlers.updateMenu
import com.example.slapimage.R
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

var smoothTabs = Settings.smooth_tabs

class TabSelectedListener(val activity: XEDMainActivity) : TabLayout.OnTabSelectedListener {

    override fun onTabSelected(tab: Tab?) {
        currentTab = WeakReference(tab)
        if (smoothTabs.not()) { activity.viewPager!!.setCurrentItem(tab!!.position, false) }
        tab?.text = tab?.text
        DefaultScope.launch { updateMenu(XEDMainActivity.activityRef.get()?.adapter?.getCurrentFragment()) }

        tab?.view?.setOnLongClickListener{ view ->
            onTabReselected(tab)
            true
        }
    }
    
    override fun onTabReselected(tab: Tab?) {
        DefaultScope.launch { updateMenu(XEDMainActivity.activityRef.get()?.adapter?.getCurrentFragment()) }

        val view = tab?.view

        if (view == null){
            errorDialog(strings.unknown_err.getString())
            return
        }

        val popupMenu = PopupMenu(activity, view)
        popupMenu.menuInflater.inflate(R.menu.tab_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
            when (id) {
                R.id.close_this -> {
                    activity.adapter!!.removeFragment(tab.position,true)
                }
                
                R.id.close_others -> {
                    activity.adapter!!.clearAllFragmentsExceptSelected()
                }
                
                R.id.close_all -> {
                    activity.adapter!!.clearAllFragments()
                }
            }
            activity.binding!!.tabs.invalidate()
            activity.binding!!.tabs.requestLayout()
            
            // Detach and re-attach the TabLayoutMediator
            TabLayoutMediator(activity.binding!!.tabs, activity.viewPager!!) { tab, position ->
                tab.text = activity.tabViewModel.fragmentTitles[position]
            }
                .attach()
            DefaultScope.launch { updateMenu(XEDMainActivity.activityRef.get()?.adapter?.getCurrentFragment()) }

            XEDMainActivity.withContext {
                for(i in 0 until tabViewModel.fragmentTitles.size){
                    tabLayout!!.getTabAt(i)?.text = tabViewModel.fragmentTitles[i]
                }
            }

            true
        }
        popupMenu.show()
    }
    override fun onTabUnselected(tab: Tab?) {

    }
}