package com.a2devel.words.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.service.UpdateService;

public class WordsWidget extends AppWidgetProvider {
	
	private static final String TAG = "WordsWidget";
	
	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	public static String ACTION_WIDGET_SETTINGS = "ActionReceiverSettings";
	
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
    	
    	RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_word);
    	Intent active = new Intent(context, WordsWidget.class);
        active.setAction(ACTION_WIDGET_REFRESH);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        view.setOnClickPendingIntent(R.id.updateButton, actionPendingIntent);
        
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
