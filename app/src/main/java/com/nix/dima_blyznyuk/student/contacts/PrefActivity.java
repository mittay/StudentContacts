package com.nix.dima_blyznyuk.student.contacts;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.nix.dima_blyznyuk.student.contacts.R;

/**
 * Created by Dima Blyznyuk on 08.07.15.
 */
public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}
