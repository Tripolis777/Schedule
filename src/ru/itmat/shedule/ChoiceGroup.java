package ru.itmat.shedule;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ChoiceGroup extends PreferenceFragment {

	 public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    addPreferencesFromResource(R.xml.preferences);
		  }
}
