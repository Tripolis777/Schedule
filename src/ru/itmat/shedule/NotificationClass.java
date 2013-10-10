package ru.itmat.shedule;

import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationClass {

	private static final String TAG = NotificationClass.class.getSimpleName();
    
	  private static NotificationClass instance;

	  private Context context;
	  private NotificationManager manager; 
	  private int lastId = 0; 
	  private HashMap<Integer, Notification> notifications; 
	  
	  
	 
	  private NotificationClass(Context context){
	    this.context = context;
	    manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    notifications = new HashMap<Integer, Notification>();
	  }

	  public static NotificationClass getInstance(Context context){
	    if(instance==null){
	        instance = new NotificationClass(context);
	    } else{
	        instance.context = context;
	    }
	    return instance;
	  }
	
	  public int createInfoNotification(String message){
		    Intent notificationIntent = new Intent(context, MainActivity.class);
		    NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.spam) 
		        .setAutoCancel(true) 
		        .setTicker(message) 
		        .setContentText(message) 
		        .setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
		        .setWhen(System.currentTimeMillis()) 
		        .setContentTitle("Расписание")
		        .setDefaults(Notification.DEFAULT_ALL); 

		        Notification notification = nb.build(); 
		        manager.notify(lastId, notification); 
		      notifications.put(lastId, notification); 
		    return lastId++;
		  }

}
