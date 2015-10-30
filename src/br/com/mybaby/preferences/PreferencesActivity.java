package br.com.mybaby.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import br.com.mybaby.R;

public class PreferencesActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		
		addPreferencesFromResource(R.xml.preferences);


	}
}
