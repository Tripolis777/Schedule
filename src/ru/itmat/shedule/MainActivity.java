package ru.itmat.shedule;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnSharedPreferenceChangeListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	String KEY_CLASS = "list_class";
	String KEY_GROUP = "list_group";
	static String TAG_INDEX = "index";
	static String URL = "http://itmat.net/sched/it/";
	static String url; 
	static String result;
	static int index = -1;
	
	ViewPager mViewPager;
	static SharedPreferences pref;
	static SharedPreferences schedule;
	
	static private final String TAG_SHEDULE = "shedule";
	static Context sContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		schedule = getPreferences(MODE_PRIVATE);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		
		sContext = getApplicationContext();
		
		if(schedule.getString(TAG_SHEDULE, "") == "") {
			if(Online()) {
				url = URL + pref.getString(KEY_CLASS, "2") +"/"+ pref.getString(KEY_GROUP, "2") + 
						"?json=" + schedule.getString("update", "1");
				Download sd = new Download();
				sd.execute(url);
			}
			startActivity(new Intent(this, Settings.class));
		}
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
		
		Toast.makeText(this, "Перед вами расписание группы: " + pref.getString(KEY_CLASS, "") + 
				"." + pref.getString(KEY_GROUP, ""), Toast.LENGTH_SHORT).show();
	}
	
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		url = URL + pref.getString(KEY_CLASS, "2") +"/"+ pref.getString(KEY_GROUP, "2") + 
					"?json=" + schedule.getString("update", "1");
		Log.v("URL", url);
			if(key.contentEquals("auto_update")) {
				if(pref.getBoolean("auto_update", false)) {
					startService(new Intent(this, AppService.class));
				} else {
					stopService(new Intent(this, AppService.class));
				}
				return;
			}
			
		if(!Online()) {
			Toast.makeText(this, "Don't have INTERNET connection.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		AppService.mId = 0;
		Download sd = new Download();
		sd.execute(url);
		}
		
	public boolean Online() {
		String cs = Context.CONNECTIVITY_SERVICE;
		  ConnectivityManager cm = (ConnectivityManager)
		    getSystemService(cs);
		  if (cm.getActiveNetworkInfo() == null) {
		    return false;
		  }
		  return     cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.bar, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.item2:
	        	Editor ed = schedule.edit();
	    	    ed.putInt(TAG_INDEX, index);
	    	    ed.commit();
	    	    recreate();
	            return true;
	        case R.id.item3:
	        	Editor ed2 = schedule.edit();
	    	    ed2.putInt(TAG_INDEX, 5);
	    	    ed2.commit();
	    	    recreate();
	    	    return true;
	        case R.id.item1:
	            startActivity(new Intent(this, Settings.class));
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 6 total pages.
			return 6;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			case 4:
				return getString(R.string.title_section5).toUpperCase(l);
			case 5:
				return getString(R.string.title_section6).toUpperCase(l);
			}
			return null;
		}
	}

	
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		public static final String[] names = {"Зайдите в настройки и выберите группу."};
		
		public DummySectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			ListView lvMain = (ListView) rootView.findViewById(R.id.list1);
			
			if(schedule.getString(TAG_SHEDULE, "") == "") {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(sContext,
					R.layout.default_list_item, names); 
			lvMain.setAdapter(adapter);
			} else {
				JSONParser json = new JSONParser(schedule.getString(TAG_SHEDULE, ""));
				PairsAdapter adapter = new PairsAdapter(sContext, json.getDayPairs(getArguments().
						getInt(ARG_SECTION_NUMBER)+schedule.getInt(TAG_INDEX, index)));
				lvMain.setAdapter(adapter);
			}
			
			return rootView;
		}
		

	}
	
}
