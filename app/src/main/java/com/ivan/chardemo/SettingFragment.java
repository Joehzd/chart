package com.ivan.chardemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by a123 on 2017/5/16.
 */

public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean temperature_unit= preferences.getBoolean("temperature_unit",true);
        System.out.println(temperature_unit+"==unit");
        Preference preference=findPreference("temperature_unit");
        Preference tip=findPreference("temperature_tip");
        Preference smartLock=findPreference("smartLock");
        smartLock.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(),"smartlock",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean) newValue){
                    System.out.println("我变true");
                }else {
                    System.out.println("我变false");
                }
                return true;
            }
        });

    }
}
