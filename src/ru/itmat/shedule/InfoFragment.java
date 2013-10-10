package ru.itmat.shedule;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoFragment extends Fragment {

	  public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		  }

		  public View onCreateView(LayoutInflater inflater, ViewGroup container,
		      Bundle savedInstanceState) {
		    return inflater.inflate(R.layout.info_layout, null);
		  }
}
