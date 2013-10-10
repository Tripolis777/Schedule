package ru.itmat.shedule;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PairsAdapter extends BaseAdapter {
	
	public HashMap<Integer, HashMap<String, String>> day;
	int height;
	Context context;
	
	private int[] colors = {Color.parseColor("#F7F7F7"), Color.parseColor("#bef574")};
	
	public PairsAdapter(Context context, HashMap<Integer, HashMap<String, String>> day) {
		if(day != null) this.day = day;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return day.size();
	}

	@Override
	public Object getItem(int num) {
		// TODO Auto-generated method stub
		return day.get(num);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View someView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		
		LayoutInflater inflater = LayoutInflater.from(context);
		
		 if (someView == null) {
	            someView = inflater.inflate(R.layout.my_list_item, parent, false);
		 }
		 
		 String aud = "Аудитория : ";
		 someView.setBackgroundColor(colors[position%2]);
		 
		 TextView tvName = (TextView) someView.findViewById(R.id.tvName);
	     TextView tvProf = (TextView) someView.findViewById(R.id.tvProf);
	     TextView tvlab = (TextView) someView.findViewById(R.id.tvlab);
	     
	     tvName.setText(day.get(position).get("name"));
	     tvProf.setText(day.get(position).get("prof"));
	     if(day.get(position).get("lab").length() > 4 || day.get(position).get("lab").length() < 3) {aud = "";}
	     tvlab.setText(aud + day.get(position).get("lab") + 
	    		 "   " + day.get(position).get("type"));

		return someView;
	}

}
