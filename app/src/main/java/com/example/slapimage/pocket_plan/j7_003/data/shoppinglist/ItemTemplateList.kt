package com.example.slapimage.pocket_plan.j7_003.data.shoppinglist

import com.example.slapimage.musicplayer.ApplicationClass
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
//import com.example.slapimage.pocket_plan.j7_003.App
import com.example.slapimage.pocket_plan.j7_003.data.settings.Languages
import com.example.slapimage.pocket_plan.j7_003.data.settings.SettingId
import com.example.slapimage.pocket_plan.j7_003.data.settings.SettingsManager

class ItemTemplateList : ArrayList<ItemTemplate>() {
    init {
        loadFromAssets()
    }

    /**
     * Returns an ItemTemplate if one was defined in the assets item_list_de.json file.
     * @param name The name the ItemTemplate is supposed to have.
     * @return Returns the template if found, null otherwise.
     */
    fun getTemplateByName(name: String): ItemTemplate? {

        this.forEach { e ->
            if (e.n.equals(name, ignoreCase = true)) {
                return e
            }
        }
        return null

    }

    private fun loadFromAssets() {
        val languageCode = when (SettingsManager.getSetting(SettingId.LANGUAGE)) {
            Languages.ROMANIAN.index -> Languages.ROMANIAN.code
            Languages.ITALIAN.index -> Languages.ITALIAN.code
            Languages.RUSSIAN.index -> Languages.RUSSIAN.code
            Languages.SPANISH.index -> Languages.SPANISH.code
            Languages.FRENCH.index -> Languages.FRENCH.code
            Languages.GERMAN.index -> Languages.GERMAN.code
            else -> Languages.ENGLISH.code
        }
        val fileName = "item_list_$languageCode.json"
        //todo add try catch here
        val jsonString = ApplicationClass.instance.assets.open(fileName).bufferedReader().readText()

        val list: ArrayList<TMPTemplate> = GsonBuilder().create()
                .fromJson(jsonString, object : TypeToken<ArrayList<TMPTemplate>>() {}.type)

        list.forEach { e ->
            this.add(ItemTemplate(e.n, e.c, "x"))
        }
    }

    private class TMPTemplate(val n: String, val c: String)
}

data class ItemTemplate(var n: String, var c: String, var s: String)
