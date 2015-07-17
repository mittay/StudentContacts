package com.nix.dimablyznyuk.student.contacts;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by Dima Blyznyuk on 13.07.15.
 */
public class LocaleUtils {
    private static int locale;

    public final static int LOCALE_EN = 0;
    public final static int LOCALE_RU = 1;
    public final static int LOCALE_DEFAULT = 2;

    public static void changeLocaleTo(Activity activity, int locale) {
        LocaleUtils.locale = locale;
        activity.finish();
        Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void onActivityCreateSetLocale(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        if (prefs.getString(PrefActivity.PREF_KEY_LOCALE,
                PrefActivity.VALUE_LOCALE_DEFAULT)
                .equals(PrefActivity.VALUE_LOCALE_ENGLISH)) {
            locale = LOCALE_EN;
        } else if (prefs.getString(PrefActivity.PREF_KEY_LOCALE,
                PrefActivity.VALUE_LOCALE_DEFAULT)
                .equals(PrefActivity.VALUE_LOCALE_RUSSIAN)) {
            locale = LOCALE_RU;
        } else {
            locale = LOCALE_DEFAULT;
        }

        switch (locale) {

            default:
            case LOCALE_EN:
                String english = PrefActivity.VALUE_LOCALE_ENGLISH;
                setupLocale(activity, english);
                break;
            case LOCALE_RU:
                String russian = PrefActivity.VALUE_LOCALE_RUSSIAN;
                setupLocale(activity, russian);
                break;
            case LOCALE_DEFAULT:
                String defaultLocale = Resources.getSystem().getConfiguration()
                        .locale.getLanguage();
                setupLocale(activity, defaultLocale);
                break;
        }
    }

    private static void setupLocale(Activity activity, String localeToLoad) {
        Locale locale = new Locale(localeToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }
}

