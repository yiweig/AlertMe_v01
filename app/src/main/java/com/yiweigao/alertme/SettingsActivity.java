package com.yiweigao.alertme;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * Created by yiweigao on 1/8/15.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenceFragment1())
                .commit();
    }

    public static class PreferenceFragment1 extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

//            PreferenceManager.setDefaultValues(getActivity(),
//                    R.xml.preferences, false);

            addPreferencesFromResource(R.xml.preferences);

        }
    }
}
