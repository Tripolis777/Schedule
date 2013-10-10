package ru.itmat.shedule;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences.Editor;
import android.util.Log;

public class JSONParser {
	
	JSONArray pairs = null;
	JSONArray days = null;
	private ArrayList<HashMap<Integer, HashMap<String, String>>> pairlist;
	private JSONObject jsonObj;
	private String update = null; 

	private static final String TAG_NAME = "name";
	private static final String TAG_PROF = "prof";
	private static final String TAG_LAB = "lab";
	private static final String TAG_TYPE = "type";
	private static final String TAG_DAYS = "days";
	private static final String TAG_PAIR = "pair";
	private static final String TAG_UPDATE = "update";
	
	public JSONParser(String json) {
		jsonParser(json);
	}
	
	public ArrayList<HashMap<Integer, HashMap<String, String>>>  getArrayList() {
		return pairlist;
	}
	
	public HashMap<Integer, HashMap<String, String>> getDayPairs(int index) {
		return pairlist.get(index);
	}
	
	public String getUpdate(){
		return update;
	}
	
	private void jsonParser(String json) {
		try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        
        pairlist = new ArrayList<HashMap<Integer, HashMap<String, String>>>();
        	
        try {
        	update = jsonObj.getString(TAG_UPDATE);
        	Editor ed = MainActivity.schedule.edit();
            ed.putString("update", update);
            ed.commit();
        	days = jsonObj.getJSONArray(TAG_DAYS);
        	
        	for(int i = 0; i < days.length(); i++) {
        		JSONObject temp = days.getJSONObject(i);
        		pairs = temp.getJSONArray(TAG_PAIR);
        		HashMap<Integer, HashMap<String, String>> day = new HashMap<Integer, HashMap<String, String>>();
        		for(int j = 0; j < pairs.length(); j++) {
        			JSONObject pair = pairs.getJSONObject(j);

        			
        			String name = pair.getString(TAG_NAME);
        			String prof = pair.getString(TAG_PROF);
        			String lab = pair.getString(TAG_LAB);
        			String type = pair.getString(TAG_TYPE);
        			
        			HashMap<String, String> map = new HashMap<String, String>();
        			
        			map.put(TAG_NAME, name);
        			map.put(TAG_PROF, prof);
        			map.put(TAG_LAB, lab);
        			map.put(TAG_TYPE, type);
        			
        			day.put(j, map);
        		}
        		pairlist.add(day);
        	}
        } catch(JSONException e) {
        	//It's bad :(
        }
	}
	
	//private void dataBaseWriter(String TAG, String value) {
		
	//}
}
