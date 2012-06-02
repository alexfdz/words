package com.a2devel.words.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.intent.ConfigurationActivity;
import com.a2devel.words.widget.WordsWidget;

public abstract class WordsService extends Service {

	private static final String TAG = "WordsService";
	
    @Override
    public void onStart(Intent intent, int startId) {
        RemoteViews view = updateView(this);
        updateCommonElements(this, intent, view);
        ComponentName widget = new ComponentName(this, WordsWidget.class);
        AppWidgetManager.getInstance(this).updateAppWidget(widget, view);
    }

    public abstract RemoteViews updateView(Context context);
    
    /**
     * @param context
     * @return
     */
    protected RemoteViews updateCommonElements(Context context, Intent intent, RemoteViews view) {
       
    	int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
    	
    	 Log.d(TAG, "appWidgetId " + appWidgetId);
    	 
        Intent active = new Intent(context, WordsWidget.class);
        active.setAction(WordsWidget.ACTION_WIDGET_REFRESH);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        view.setOnClickPendingIntent(R.id.updateButton, actionPendingIntent);
        
  
        Intent settingIntent = new Intent(context, ConfigurationActivity.class);
		settingIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
		Bundle extras = new Bundle();
		extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		settingIntent.putExtras(extras);
		settingIntent.setData(Uri.parse(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
						+ appWidgetId));

		PendingIntent pendingIntentSettings = PendingIntent.getActivity(
				context, 0, settingIntent, 0);

		view.setOnClickPendingIntent(R.id.settingsButton, pendingIntentSettings);
		
        
        Log.d(TAG, "Updated common elements");
        
        return view;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}