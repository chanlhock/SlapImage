package com.example.slapimage.textpad.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.example.slapimage.R;
import com.example.slapimage.textpad.service.SettingsService;

public class BackgroundColorPreference extends ColorPreference {
    public BackgroundColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        title = context.getResources().getString(R.string.Choose_a_background_color);
    }

    protected void saveColor(int color) {
        settingsService.setBgColor(color, getContext());
    }

    protected void initColor() {
        color = settingsService.getBgColor();
    }

    protected int getDefaultColor() {
        return SettingsService.DEFAULT_BACKGROUND_COLOR;
    }
}
