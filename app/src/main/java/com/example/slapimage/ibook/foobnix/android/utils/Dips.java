/**
 *
 */
package com.example.slapimage.ibook.foobnix.android.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.view.WindowMetrics;

import com.example.slapimage.musicplayer.ApplicationClass;

import java.util.Locale;

public class Dips {
    // It's good practice to make the context field private and provide a getter or ensure it's only used internally.
    @SuppressLint("StaticFieldLeak") // Suppress if using Application context as intended
    private static Context applicationContext;

    private static final String TAG = "Dips"; // For logging
    public final static int DP_0 = 0;
    public final static int DP_1 = Dips.dpToPx(1);
    public final static int DP_2 = Dips.dpToPx(2);
    public final static int DP_3 = Dips.dpToPx(3);
    public final static int DP_4 = Dips.dpToPx(4);
    public final static int DP_5 = Dips.dpToPx(5);
    public final static int DP_6 = Dips.dpToPx(6);
    public final static int DP_8 = Dips.dpToPx(8);
    public final static int DP_10 = Dips.dpToPx(10);
    public final static int DP_15 = Dips.dpToPx(15);
    public final static int DP_20 = Dips.dpToPx(20);
    public final static int DP_25 = Dips.dpToPx(25);
    public final static int DP_32 = Dips.dpToPx(32);
    public final static int DP_36 = Dips.dpToPx(36);
    public final static int DP_40 = Dips.dpToPx(40);
    public final static int DP_45 = Dips.dpToPx(45);
    public final static int DP_46 = Dips.dpToPx(46);
    public final static int DP_48 = Dips.dpToPx(48);
    public final static int DP_50 = Dips.dpToPx(50);
    public final static int DP_60 = Dips.dpToPx(60);
    public final static int DP_80 = Dips.dpToPx(80);
    public final static int DP_90 = Dips.dpToPx(90);
    public final static int DP_100 = Dips.dpToPx(100);
    public final static int DP_150 = Dips.dpToPx(150);

    public final static int DP_120 = Dips.dpToPx(120);
    public final static int DP_800 = Dips.dpToPx(800);
    public final static int DP_600 = Dips.dpToPx(600);
    public final static int DP_400 = Dips.dpToPx(400);
    public final static int DP_300 = Dips.dpToPx(300);
    public final static int DP_200 = Dips.dpToPx(200);
    public final static int DP_1000 = Dips.dpToPx(1000);
    public static Activity myactivity;
    //private static WindowManager wm;

   // public static void init(Context context) {
   //     Dips.context = context;
       // wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
   // }
    public static void init(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Log.w(TAG, "Dips class is designed for API 29+ but current API is " + Build.VERSION.SDK_INT);
            // You could throw an error or handle this gracefully if it's a strict requirement.
        }
     //   if (activity == null) {
     //       Log.e(TAG, "Dips.init() called with a null context!");
            // applicationContext will remain null, methods need to handle or rely on System Resources
     //       return;
     //   }
        myactivity = activity;
        // Use application context to avoid leaks and for global application resources.
        applicationContext = ApplicationClass.context;
        Log.i(TAG, "Dips initialized successfully.");
    }

    private static DisplayMetrics getSafeDisplayMetrics() {
        if (applicationContext != null) {
            return applicationContext.getResources().getDisplayMetrics();
        }
        // Fallback if init wasn't called or context was null
        Log.w(TAG, "applicationContext is null. Using system resources for DisplayMetrics.");
        return Resources.getSystem().getDisplayMetrics();
    }

   /* public static int spToPx(final int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    public static int dpToPx(final int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(final int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
*/
   public static int spToPx(final int sp) {
       return (int) (sp * getSafeDisplayMetrics().scaledDensity + 0.5f);
   }

    public static int dpToPx(final int dp) {
        return (int) (dp * getSafeDisplayMetrics().density + 0.5f);
    }

    public static int pxToDp(final int px) {
        return (int) (px / getSafeDisplayMetrics().density + 0.5f);
    }


    // @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  //  @Deprecated
  //  public static int screenWidth() {
  //   //   if (Build.VERSION.SDK_INT >= 17) {
  //          try {
  //              wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
  //              Point size = new Point();
  //              wm.getDefaultDisplay().getRealSize(size);
//return size.x;
  //          } catch (Exception e) {
 //               return Resources.getSystem().getDisplayMetrics().widthPixels;
 //           }
     //   } else {
     //       return Resources.getSystem().getDisplayMetrics().widthPixels;
     //   }
//    }
    /**
     * Gets the current screen width in pixels.
     * Optimized for API 29 (Android 10) or higher.
     *
     * @return Screen width in pixels, or a fallback based on system/application DisplayMetrics.
     */
    public static int screenWidth() {
        if (myactivity == null) {
            Log.e(TAG, "screenWidth: applicationContext is null. Call Dips.init() first. " +
                    "Falling back to system DisplayMetrics width.");
            return Resources.getSystem().getDisplayMetrics().widthPixels;
        }

        WindowManager wm = (WindowManager) myactivity.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            Log.e(TAG, "screenWidth: WindowManager service not available. " +
                    "Falling back to application DisplayMetrics width.");
            return applicationContext.getResources().getDisplayMetrics().widthPixels;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 30+
            try {
                WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
                Rect bounds = windowMetrics.getBounds();
                return bounds.width();
            } catch (Exception e) {
                Log.e(TAG, "Error getting screen width via WindowMetrics (API 30+). Falling back.", e);
                return applicationContext.getResources().getDisplayMetrics().widthPixels;
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) { // API 29 (Q)
            try {
                // For API 29, getDefaultDisplay().getRealMetrics() is a good option.
                DisplayMetrics displayMetrics = new DisplayMetrics();
                @SuppressWarnings("deprecation") // getDefaultDisplay is used for API 29.
                Display display = wm.getDefaultDisplay();
                if (display != null) {
                    display.getRealMetrics(displayMetrics);
                    return displayMetrics.widthPixels;
                } else {
                    Log.e(TAG, "screenWidth (API 29): getDefaultDisplay() returned null. Falling back.");
                    return applicationContext.getResources().getDisplayMetrics().widthPixels;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting screen width (API 29). Falling back.", e);
                return applicationContext.getResources().getDisplayMetrics().widthPixels;
            }
        } else {
            // Should not happen if app's minSdk is Q or Dips.init() checks version.
            Log.w(TAG, "screenWidth: Called on API version lower than Q (" + Build.VERSION.SDK_INT + "). " +
                    "Falling back to application DisplayMetrics width.");
            return applicationContext.getResources().getDisplayMetrics().widthPixels;
        }
    }
 /*   public static float getRefreshRate() {
        try {
            final Display display = wm.getDefaultDisplay();
            float refreshRate = display.getRefreshRate();
            LOG.d("RefreshRate", refreshRate);
            return refreshRate;
        } catch (Exception e) {
            LOG.e(e);
            return 60;
        }
    }
*/
    /**
     * Gets the refresh rate of the default display.
     * Requires API 23 (M) for Display.getMode().getRefreshRate(),
     * API 30 (R) for Display.getRefreshRate() directly.
     * Prefers API 30 method if available.
     *
     * @return The display refresh rate in frames per second, or 60.0f as a fallback.
     */
    public static float getRefreshRate() {
        if (myactivity == null) {
            Log.e(TAG, "getRefreshRate: applicationContext is null. Call Dips.init() first. Defaulting to 60fps.");
            return 60.0f;
        }
        WindowManager wm = (WindowManager) myactivity.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            Log.e(TAG, "getRefreshRate: WindowManager service not available. Defaulting to 60fps.");
            return 60.0f;
        }

        try {
            @SuppressWarnings("deprecation") // getDefaultDisplay is used as part of the logic for Q and R.
            Display display = wm.getDefaultDisplay();
            if (display != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 30+
                    return display.getRefreshRate();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // API 23-29 (M to Q)
                    // Display.Mode is available from API 23+
                    Display.Mode mode = display.getMode();
                    if (mode != null) {
                        return mode.getRefreshRate();
                    }
                }
                // Fallback for older APIs (though this class targets Q+) or if mode is null
                Log.w(TAG, "getRefreshRate: Could not determine refresh rate via modern APIs. Using Display.getRefreshRate() (may be less accurate or default).");
                return display.getRefreshRate(); // This is a generic float, might be a default on older devices.
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting refresh rate. Defaulting to 60fps.", e);
        }
        return 60.0f; // Default fallback
    }
    /*public static boolean isDarkThemeOn() {
        try {
            return (Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        } catch (Exception e) {
            return false;
        }

    }


    public static boolean isEInk() {
        
        boolean isEink = getRefreshRate() < 30.0;
        if (isEink) {
            return true;
        }

        String brand = Build.BRAND.toLowerCase(Locale.US);
        if (
                brand.contains("unknown") ||
                        brand.contains("icarus") ||
                        brand.contains("nook") ||
                        brand.contains("inkbook") ||
                        brand.contains("boyue") ||
                        brand.contains("boeye") ||
                        brand.contains("energysistem") ||
                        brand.contains("crema") ||
                        brand.contains("energy") ||
                        brand.contains("onyx") ||
                        brand.contains("tolino") ||
                        brand.contains("likebook")) {
            return true;
        }
        return false;
    }
*/
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
   // @Deprecated
  //  public static int screenHeight() {
       // if (Build.VERSION.SDK_INT >= 17) {
   //         try {
   //             wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
  //              Point size = new Point();
  //              wm.getDefaultDisplay().getRealSize(size);
  //              return size.y;
//} catch (Exception e) {
  //              return Resources.getSystem().getDisplayMetrics().heightPixels;
  //          }
      //  } else {
     //       return Resources.getSystem().getDisplayMetrics().heightPixels;
      //  }
 //   }

    /**
     * Gets the current screen height in pixels.
     * Optimized for API 29 (Android 10) or higher.
     *
     * @return Screen height in pixels, or a fallback based on system/application DisplayMetrics.
     */
    public static int screenHeight() {
        if (myactivity == null) {
            Log.e(TAG, "screenHeight: applicationContext is null. Call Dips.init() first. " +
                    "Falling back to system DisplayMetrics height.");
            return Resources.getSystem().getDisplayMetrics().heightPixels;
        }

        WindowManager wm = (WindowManager) myactivity.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            Log.e(TAG, "screenHeight: WindowManager service not available. " +
                    "Falling back to application DisplayMetrics height.");
            return applicationContext.getResources().getDisplayMetrics().heightPixels;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 30+
            try {
                WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
                Rect bounds = windowMetrics.getBounds();
                return bounds.height();
            } catch (Exception e) {
                Log.e(TAG, "Error getting screen height via WindowMetrics (API 30+). Falling back.", e);
                return applicationContext.getResources().getDisplayMetrics().heightPixels;
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) { // API 29 (Q)
            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                @SuppressWarnings("deprecation") // getDefaultDisplay is used for API 29.
                Display display = wm.getDefaultDisplay();
                if (display != null) {
                    display.getRealMetrics(displayMetrics);
                    return displayMetrics.heightPixels;
                } else {
                    Log.e(TAG, "screenHeight (API 29): getDefaultDisplay() returned null. Falling back.");
                    return applicationContext.getResources().getDisplayMetrics().heightPixels;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting screen height (API 29). Falling back.", e);
                return applicationContext.getResources().getDisplayMetrics().heightPixels;
            }
        } else {
            // Should not happen if app's minSdk is Q or Dips.init() checks version.
            Log.w(TAG, "screenHeight: Called on API version lower than Q (" + Build.VERSION.SDK_INT + "). " +
                    "Falling back to application DisplayMetrics height.");
            return applicationContext.getResources().getDisplayMetrics().heightPixels;
        }
    }

    public static int screenWidthDP() {
        return pxToDp(screenWidth());
    }

    public static int screenHeightDP() {
        return pxToDp(screenHeight());
    }

    public static int screenMinWH() {
        return Math.min(screenHeight(), screenWidth());
    }

    public static int screenMinWHDp() {
        return pxToDp(screenMinWH());
    }

    public static boolean isDarkThemeOn() {
        // Use application context's resources if available, otherwise system resources.
        Resources resources;
        if (applicationContext != null) {
            resources = applicationContext.getResources();
        } else {
            Log.w(TAG, "isDarkThemeOn: applicationContext is null. Using system resources. Call Dips.init() first.");
            resources = Resources.getSystem();
        }

        try {
            return (resources.getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        } catch (Exception e) {
            Log.e(TAG, "Error checking dark theme status. Defaulting to false.", e);
            return false;
        }
    }
    /**
     * Attempts to determine if the device is an E-Ink device.
     * This is a heuristic and may not be 100% accurate.
     * It checks refresh rate and common E-Ink device brand names.
     *
     * @return true if the device is likely E-Ink, false otherwise.
     */
    public static boolean isEInk() {
        // Check 1: Low refresh rate (common for E-Ink displays)
        // Ensure getRefreshRate() is called after Dips.init()
        if (getRefreshRate() < 30.0f && getRefreshRate() > 0) { // Check > 0 to avoid default 0fps if error
            Log.i(TAG, "isEInk: Detected low refresh rate (" + getRefreshRate() + "fps), likely E-Ink.");
            return true;
        }

        // Check 2: Device Brand/Model (less reliable but can be a hint)
        // This list can be expanded or refined based on known E-Ink devices.
        String brand = Build.BRAND.toLowerCase(Locale.US);
        String model = Build.MODEL.toLowerCase(Locale.US);
        String device = Build.DEVICE.toLowerCase(Locale.US);
        String product = Build.PRODUCT.toLowerCase(Locale.US);


        String[] einkBrands = {
                "onyx", "boox", "likebook", "boyue", "tolino", "kobo", "remarkable",
                "pocketbook", "nook", "kindle", "icarus", "inkbook", "crema", "hanvon", "dasung"
        };

        for (String einkBrand : einkBrands) {
            if (brand.contains(einkBrand) || model.contains(einkBrand) || device.contains(einkBrand) || product.contains(einkBrand)) {
                Log.i(TAG, "isEInk: Detected E-Ink brand/model keyword: " + einkBrand + " in " + brand + "/" + model);
                return true;
            }
        }
        // Add specific model checks if necessary, e.g., if (Build.MODEL.startsWith("DPT-")) return true; // Sony DPT

        Log.i(TAG, "isEInk: No strong indicators of E-Ink display found for brand: " + brand + ", model: " + model);
        return false;
    }
/*
    public static boolean isSmallScreen() {
        // large screens are at least 640dp x 480dp
        return Dips.screenMinWH() < Dips.dpToPx(450);
    }

    public static boolean isSmallWidth() {
        return screenWidth() < Dips.dpToPx(450);
    }

    public static boolean isHorizontal() {
        return screenWidth() > screenHeight();
    }

    public static boolean isVertical() {
        return screenWidth() < screenHeight();
    }
*/
public static boolean isSmallScreen() {
    // Defines "small" based on a minimum dimension in DP.
    // For API 29+, screen configuration qualifiers (e.g., sw<N>dp) are preferred for layout resources.
    // This can be a runtime check if needed.
    return screenMinWHDp() < 450; // Example threshold: less than 450dp on the smallest width
}

    public static boolean isSmallWidth() {
        // Checks if the current screen width in DP is considered "small".
        return screenWidthDP() < 450; // Example threshold
    }

    public static boolean isHorizontal() {
        return screenWidth() > screenHeight();
    }

    public static boolean isVertical() {
        return screenWidth() < screenHeight();
    }
/*
    public static boolean isXLargeScreen() {
        int size = Resources.getSystem().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        // return size == Configuration.SCREENLAYOUT_SIZE_LARGE || size ==
        // Configuration.SCREENLAYOUT_SIZE_XLARGE;
        return size == Configuration.SCREENLAYOUT_SIZE_XLARGE;

    }

    public static boolean isLargeOrXLargeScreen() {
        int size = Resources.getSystem().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        return size == Configuration.SCREENLAYOUT_SIZE_LARGE || size == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static boolean isSystemAutoRotation(Context c) {
        try {
            return android.provider.Settings.System.getInt(c.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        } catch (Exception e) {
            return true;
        }
    }

    public static int geUserRotation(Context c) {
        try {
            return android.provider.Settings.System.getInt(c.getContentResolver(), Settings.System.USER_ROTATION, 0);
        } catch (Exception e) {
            return Surface.ROTATION_90;
        }
    }
*/
    /**
     * Checks if the screen layout size is classified as XLARGE.
     * Uses system configuration.
     *
     * @return true if the screen is XLARGE, false otherwise.
     */
    public static boolean isXLargeScreen() {
        // Use application context's resources if available, otherwise system resources.
        Resources resources;
        if (applicationContext != null) {
            resources = applicationContext.getResources();
        } else {
            Log.w(TAG, "isXLargeScreen: applicationContext is null. Using system resources. Call Dips.init() first.");
            resources = Resources.getSystem();
        }
        try {
            int screenSize = resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            return screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
        } catch (Exception e) {
            Log.e(TAG, "Error checking xlarge screen status. Defaulting to false.", e);
            return false;
        }
    }

    /**
     * Checks if the screen layout size is classified as LARGE or XLARGE.
     * Uses system configuration.
     *
     * @return true if the screen is LARGE or XLARGE, false otherwise.
     */
    public static boolean isLargeOrXLargeScreen() {
        Resources resources;
        if (applicationContext != null) {
            resources = applicationContext.getResources();
        } else {
            Log.w(TAG, "isLargeOrXLargeScreen: applicationContext is null. Using system resources. Call Dips.init() first.");
            resources = Resources.getSystem();
        }
        try {
            int screenSize = resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
        } catch (Exception e) {
            Log.e(TAG, "Error checking large/xlarge screen status. Defaulting to false.", e);
            return false;
        }
    }

    /**
     * Checks if system auto-rotation is enabled.
     *
     * @return true if auto-rotation is enabled, false otherwise. Defaults to true on error.
     */
    public static boolean isSystemAutoRotation() {
        if (applicationContext == null) {
            Log.w(TAG, "isSystemAutoRotation: applicationContext is null. Cannot get ContentResolver. Defaulting to true.");
            return true; // Default to true as a safe bet if context is missing
        }
        try {
            return Settings.System.getInt(applicationContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        } catch (Exception e) {
            Log.e(TAG, "Error checking system auto-rotation. Defaulting to true.", e);
            return true; // Default behavior
        }
    }

    /**
     * Gets the user-defined screen rotation setting.
     * See {@link android.view.Surface} for ROTATION_ values (ROTATION_0, ROTATION_90, etc.).
     *
     * @return The user rotation setting, or Surface.ROTATION_0 as a default on error or if context is missing.
     */
    public static int getUserRotation() {
        if (applicationContext == null) {
            Log.w(TAG, "getUserRotation: applicationContext is null. Cannot get ContentResolver. Defaulting to ROTATION_0.");
            return Surface.ROTATION_0; // Default to natural orientation
        }
        try {
            return Settings.System.getInt(applicationContext.getContentResolver(), Settings.System.USER_ROTATION, Surface.ROTATION_0);
        } catch (Exception e) {
            Log.e(TAG, "Error getting user rotation. Defaulting to ROTATION_0.", e);
            return Surface.ROTATION_0; // Default to natural orientation
        }
    }
}
