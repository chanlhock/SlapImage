package com.example.slapimage.textpad;

import android.content.Context;

import com.example.slapimage.textpad.service.AlternativeUrlsService;
import com.example.slapimage.textpad.service.RecentFilesService;
import com.example.slapimage.textpad.service.SettingsService;
import com.example.slapimage.textpad.service.ThemeService;
import com.example.slapimage.textpad.service.WakeLockService;

public class ServiceLocator {
    private static ServiceLocator instance = null;

    private ServiceLocator() {}

    private SettingsService settingsService;

    private RecentFilesService recentFilesService;

    private AlternativeUrlsService alternativeUrlsService;

    private ThemeService themeService;

    private WakeLockService wakeLockService = null;

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    public SettingsService getSettingsService(Context context) {
        if (settingsService == null) {
            settingsService = new SettingsService();
            settingsService.loadSettings(context);
        }
        return settingsService;
    }

    public RecentFilesService getRecentFilesService() {
        if (recentFilesService == null) {
            recentFilesService = new RecentFilesService();
        }
        return recentFilesService;
    }

    public AlternativeUrlsService getAlternativeUrlsService() {
        if (alternativeUrlsService == null) {
            alternativeUrlsService = new AlternativeUrlsService();
        }
        return alternativeUrlsService;
    }

    public ThemeService getThemeService(Context context) {
        if (themeService == null) {
            themeService = new ThemeService(getSettingsService(context));
        }
        return themeService;
    }

    public WakeLockService getWakeLockService() {
        if (wakeLockService == null) {
            wakeLockService = new WakeLockService();
        }
        return wakeLockService;
    }


}
