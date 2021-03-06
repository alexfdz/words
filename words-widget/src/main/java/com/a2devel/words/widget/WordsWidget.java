package com.a2devel.words.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.a2devel.words.service.UpdateService;

/**
 * Widget provider to manage the recived actions
 * 
 * @author alex
 */
public class WordsWidget extends AppWidgetProvider {
	
	private static final String TAG = "WordsWidget";
	public static String ACTION_WIDGET_REFRESH = "ActionReceiverRefresh";
	public static String URI_SCHEME = WordsWidget.class.getName();
	public static final String WORD_DATA_KEY = "com.a2devel.words.word";
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d(TAG, "onReceive action: " +intent.getAction() + " widgetId: " +intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
				AppWidgetManager.INVALID_APPWIDGET_ID));
        if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {
        	int widgetId =intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
					AppWidgetManager.INVALID_APPWIDGET_ID);
        	Intent updateIntent = new Intent(context, UpdateService.class);
            addIntentData(updateIntent, widgetId);
            context.startService(updateIntent);
        }else {
            super.onReceive(context, intent);
        }
    }
    
    /**
     * Utility to add data to the given {@link Intent} entity
     * to identify the widget.
     * @param intent
     * @param appWidgetId
     */
    public static void addIntentData(Intent intent, int appWidgetId){
    	Uri data = Uri.withAppendedPath(Uri.parse(WordsWidget.URI_SCHEME + "://widget/id/")
			    ,String.valueOf(appWidgetId));
    	intent.setData(data);
    	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    }
    
	/**
	 * Each time an instance is removed, we cancel the associated AlarmManager.
	 */
	@Override
	public void onDeleted(Context context, int[] widgetIds) {
		super.onDeleted(context, widgetIds);
		for (int widgetId : widgetIds) {
			cancelAlarm(context, widgetId);
		}
	}

	/**
	 * Cancel the alarm manager intents for a given widget
	 * @param context
	 * @param appWidgetId
	 */
	protected void cancelAlarm(Context context, int widgetId) {
		Intent active = new Intent(context, WordsWidget.class);
        active.setAction(WordsWidget.ACTION_WIDGET_REFRESH);
        WordsWidget.addIntentData(active, widgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		Log.d(TAG, "Alarm canceled:" +widgetId);
	}
	
}
