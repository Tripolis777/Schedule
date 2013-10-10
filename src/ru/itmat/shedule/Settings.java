package ru.itmat.shedule;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class Settings extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	 public void onBuildHeaders(List<Header> target) {
	        loadHeadersFromResource(R.xml.header, target);
	      }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	     // Respond to the action bar's Up/Home button
	     case android.R.id.home:
	         startActivity(new Intent(this, MainActivity.class));
	         return true;
	     }
	     return super.onOptionsItemSelected(item);
	 }
	 
}
