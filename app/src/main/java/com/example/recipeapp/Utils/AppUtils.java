package com.example.recipeapp.Utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class AppUtils {
    private static AppUtils instance;

    public AppUtils() {
        // Private constructor to prevent instantiation.
    }

    public static AppUtils getInstance() {
        if (instance == null) {
            instance = new AppUtils();
        }
        return instance;
    }

    public void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    // Add other common methods or utilities here
}