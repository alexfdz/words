package com.a2devel.words.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
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
    	if(intent != null){
    		int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
            		AppWidgetManager.INVALID_APPWIDGET_ID);
            
        	if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            	RemoteViews view = updateView(this, widgetId);
                updateCommonElements(this, widgetId, view);
            }
    	}
    }

    public abstract RemoteViews updateView(Context context, int widgetId);
    
    /**
     * @param context
     * @return
     */
    protected RemoteViews updateCommonElements(Context context, int widgetId, RemoteViews view) {
       
    	Log.d(TAG, "appWidgetId " + widgetId);
    	 
        Intent active = new Intent(context, WordsWidget.class);
        active.setAction(WordsWidget.ACTION_WIDGET_REFRESH);
        WordsWidget.addIntentData(active, widgetId);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        view.setOnClickPendingIntent(R.id.updateButton, actionPendingIntent);
        
        Log.d(TAG, "updated updatebutton to  widgetId: " + widgetId);
        
        Intent settingIntent = new Intent(context, ConfigurationActivity.class);
		settingIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
		WordsWidget.addIntentData(settingIntent, widgetId);
		PendingIntent pendingIntentSettings = PendingIntent.getActivity(
				context, 0, settingIntent, 0);

		view.setOnClickPendingIntent(R.id.settingsButton, pendingIntentSettings);
		
		Log.d(TAG, "updated settingsButton to  widgetId: " + widgetId);
		
		AppWidgetManager.getInstance(context).updateAppWidget(widgetId, view);
		
        Log.d(TAG, "Updated common elements widgetId: " + widgetId);
        
        return view;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}