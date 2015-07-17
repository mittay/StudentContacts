package com.nix.dimablyznyuk.student.contacts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Dima Blyznyuk on 13.07.15.
 */
public class ThemeUtils {
    private static int theme;

    public final static int THEME_LIGTH = 0;
    public final static int THEME_DARK = 1;

    public static void changeToTheme(Activity activity, int theme) {
        ThemeUtils.theme = theme;
        activity.finish();
        Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);

    }

    public static void onActivityCreateSetTheme(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        if (prefs.getString(PrefActivity.PREF_KEY_BUTTON_THEME,
                PrefActivity.VALUE_BUTTON_STANDART)
                .equals(PrefActivity.VALUE_BUTTON_LIGTH)) {
            theme = THEME_LIGTH;
        } else {
            theme = THEME_DARK;
        }
        switch (theme) {

            default:
            case THEME_LIGTH:
                activity.setTheme(R.style.LigthButtonTheme);
                break;
            case THEME_DARK:
                activity.setTheme(R.style.GreyButtonTheme);
                break;
        }
    }
}

