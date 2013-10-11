package ru.itmat.shedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

public class AppService extends Service {

	private Timer timer;
	static Context context;
	static int mId;
	static String url;
	static String result;
	
	public void onCreate() {
			Log.v("Service", "Start Service");
			timer = new Timer();
			url = MainActivity.url;
			context = MainActivity.sContext;
			mId = 0;
		    super.onCreate();
		  }
		  
	public int onStartCommand(Intent intent, int flags, int startId) {
			timer.schedule(new TimerUpdateTask(), 0, 3600000);
		    return super.onStartCommand(intent, flags, startId);
		  }
			
	public void onDestroy() {
		 
		  }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	class TimerUpdateTask extends TimerTask {
		
		public boolean Online() {
			String cs = Context.CONNECTIVITY_SERVICE;
			  ConnectivityManager cm = (ConnectivityManager)
			    getSystemService(cs);
			  if (cm.getActiveNetworkInfo() == null) {
			    return false;
			  }
			  return     cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}
		

		String KEY_CLASS = "list_class";
		String KEY_GROUP = "list_group";
		String TAG_SCHEDULE = "shedule";
		String URL = "http://itmat.net/sched/it/";
		
		public void run(){
			if(!Online()) return;
			if(mId == 0) { mId++; return; }
			mId++;
			url = URL + MainActivity.pref.getString(KEY_CLASS, "2") +"/"+ MainActivity.pref.getString(KEY_GROUP, "2") + 
    				"?json=" + MainActivity.schedule.getString("update", "1");
			//Download check = new Download();
			//check.execute(url);
			Download();
			Log.v("Timer", "TikYak = " + result.charAt(0));
			if(result.length() > 100 & result.charAt(0) == '{') {
				Editor ed = MainActivity.schedule.edit();
				ed.putString(TAG_SCHEDULE, result);
				ed.commit();
				NotificationClass n = NotificationClass.getInstance(context);
				n.createInfoNotification("���������� ����� ������ ���� ��������.");
			}
		}
		
		
		public void Download() {
			InputStream is = null;
			try {
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	 
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();           
	            
	        } catch (UnsupportedEncodingException e) {
	        	Log.v("Download", "UEE Error");
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	        	Log.v("Download", "CPE Error");
	            e.printStackTrace();
	        } catch (IOException e) {
	        	Log.v("Download", "IOE Error");
	            e.printStackTrace();
	        }
	         
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "UTF-8"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            result = sb.toString();
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
		}
	}
}
