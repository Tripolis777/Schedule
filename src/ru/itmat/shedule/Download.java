package ru.itmat.shedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Download extends AsyncTask<String, Long, String> {
	
	String TAG_SCHEDULE = "shedule";
    String schedule = null;
    InputStream is = null;
    long downloaded, total;

	@Override
    protected void onPreExecute() {
      super.onPreExecute();
    }
	
	@Override
    protected String doInBackground(String... urls) {
		try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urls[0]);
 
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
            schedule = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
	 	
 	return schedule;
    }

 @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      if(result.charAt(0) != '{') { Toast.makeText(MainActivity.sContext, "Ошибка сервера. Повторите попытку позже.", Toast.LENGTH_SHORT).show(); return;}
      if(result.length() > 100){
      Editor ed = MainActivity.schedule.edit();
	  ed.putString(TAG_SCHEDULE, result);
	  ed.commit();
	  }
      AppService.result = result;
    }
}
