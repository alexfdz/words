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
	
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
    	
    	for (int appWidgetId : appWidgetIds) {
    		Log.i(TAG, "onUpdate appWidgetId:"+appWidgetId);
    		Intent updateIntent = new Intent(context, UpdateService.class);
    		updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            context.startService(updateIntent);
		}
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.i(TAG, "onReceive");
        if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
            Log.i(TAG, ACTION_WIDGET_REFRESH + " EXTRA_APPWIDGET_ID:" + intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
					AppWidgetManager.INVALID_APPWIDGET_ID));
            Intent updateIntent = new Intent(context, UpdateService.class);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
    						AppWidgetManager.INVALID_APPWIDGET_ID));
            context.startService(updateIntent);
        } else {
            super.onReceive(context, intent);
        }
    }
    
}
