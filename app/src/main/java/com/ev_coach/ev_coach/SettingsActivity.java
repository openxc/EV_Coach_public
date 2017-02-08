package com.ev_coach.ev_coach;

import android.app.Activity;
import android.os.Bundle;

/**
 * Settings Activity which uses the SettingsFragment through the FragmentManager to output it's
 * layout.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTheme(R.style.AppBaseTheme);
        getFragmentManager().beginTransaction()
                .replace(R.id.activity_settings, new SettingsFragment())
                .commit();
    }
}
