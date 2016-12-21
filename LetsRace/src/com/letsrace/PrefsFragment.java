package com.letsrace;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.letsrace.R;

public class PrefsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
	}
}