package com.a2devel.words.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.a2devel.words.service.UpdateService;

public class WordsWidget extends AppWidgetProvider {
	
	private static final String TAG = "WordsWidget";
	
	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	public static String URI_SCHEME = WordsWidget.class.getName();
	
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
    	
    	for (int appWidgetId : appWidgetIds) {
    		Log.d(TAG, "onUpdate appWidgetId:" +appWidgetId);
    		Intent updateIntent = new Intent(context, UpdateService.class);
    		WordsWidget.addIntentData(updateIntent, appWidgetId);
            context.startService(updateIntent);
		}
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d(TAG, "onReceive action: " +intent.getAction() + " widgetId: " +intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
				AppWidgetManager.INVALID_APPWIDGET_ID));
        if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
        	int appWidgetId =intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
					AppWidgetManager.INVALID_APPWIDGET_ID);
            Intent updateIntent = new Intent(context, UpdateService.class);
            WordsWidget.addIntentData(updateIntent, appWidgetId);
            context.startService(updateIntent);
        } else {
            super.onReceive(context, intent);
        }
    }
    
    /**
     * @param intent
     * @param appWidgetId
     */
    public static void addIntentData(Intent intent, int appWidgetId){
    	Uri data = Uri.withAppendedPath(Uri.parse(WordsWidget.URI_SCHEME + "://widget/id/")
			    ,String.valueOf(appWidgetId));
    	intent.setData(data);
    	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    }
}
