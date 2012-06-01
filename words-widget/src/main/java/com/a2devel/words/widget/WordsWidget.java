package com.a2devel.words.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.a2devel.words.service.UpdateService;

public class WordsWidget extends AppWidgetProvider {
	
	private static final String TAG = "WordsWidget";
	
	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	public static String ACTION_WIDGET_SETTINGS = "ActionReceiverSettings";
	
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
    	
    	Log.i(TAG, "onUpdate");
        context.startService(new Intent(context, UpdateService.class));
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.i(TAG, "onReceive");
        if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
            Log.i(TAG, ACTION_WIDGET_REFRESH);
            context.startService(new Intent(context, UpdateService.class));
        } else if (intent.getAction().equals(ACTION_WIDGET_SETTINGS)) {
            Log.i(TAG, ACTION_WIDGET_SETTINGS);
        } else {
            super.onReceive(context, intent);
        }
    }

}
