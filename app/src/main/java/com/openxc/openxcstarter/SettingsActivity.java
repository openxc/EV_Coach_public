package com.openxc.openxcstarter;

import android.app.Activity;
import android.os.Bundle;

import com.openxcplatform.openxcstarter.R;

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
