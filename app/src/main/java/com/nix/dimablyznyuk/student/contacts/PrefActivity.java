package com.nix.dimablyznyuk.student.contacts;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Dima Blyznyuk on 08.07.15.
 */
public class PrefActivity extends PreferenceActivity {

    public static final String PREF_KEY_MALE_COLOR = "male_color";
    public static final String PREF_KEY_FEMALE_COLOR = "female_color";
    public static final String PREF_KEY_GENDER_FILTER = "gender_filter";
    public static final String PREF_KEY_LOCALE = "locale";
    public static final String PREF_KEY_BUTTON_THEME = "button_style";

    public static final String VALUE_BUTTON_STANDART = "0";
    public static final String VALUE_BUTTON_LIGTH = "1";
    public static final String VALUE_BUTTON_DARK = "2";

    public static final String VALUE_LOCALE_DEFAULT = "default";
    public static final String VALUE_LOCALE_ENGLISH = "en";
    public static final String VALUE_LOCALE_RUSSIAN = "ru";

    public static final String DEFAULT_GENDER_FILTER = "0";
    public static final String VALUE_GENDER_MALE = "1";
    public static final String VALUE_GENDER_FEMALE = "2";
    public static final String DEFAULT_COLOR = "#000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

    }
}
