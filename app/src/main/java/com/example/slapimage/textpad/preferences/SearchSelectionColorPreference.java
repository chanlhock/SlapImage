package com.example.slapimage.textpad.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.example.slapimage.R;
import com.example.slapimage.textpad.service.SettingsService;

public class SearchSelectionColorPreference extends ColorPreference {
    public SearchSelectionColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        title = context.getResources().getString(R.string.preferenceChooseSearchSelectionColor);
    }

    protected void saveColor(int color) {
        settingsService.setSearchSelectionColor(color, getContext());
    }

    protected void initColor() {
        color = settingsService.getSearchSelectionColor();
    }

    protected int getDefaultColor() {
        return SettingsService.DEFAULT_SEARCH_SELECTION_COLOR;
    }
}
