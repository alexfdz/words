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
import com.a2devel.words.to.Word;
import com.a2devel.words.widget.WordsWidget;

/**
 * Service with common features of widget interaction services like
 * update common elements in the view.
 * 
 * @author alex
 */
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

    /**
     * Update the view with the specific behavior of the service
     * @param context
     * @param widgetId
     * @return
     */
    public abstract RemoteViews updateView(Context context, int widgetId);
    
    /**
     * Update common elements in the widget view
     * @param context
     * @return
     */
    protected RemoteViews updateCommonElements(Context context, int widgetId, RemoteViews view) {
    	Log.d(TAG, "appWidgetId " + widgetId);
    	 
        Intent intent = new Intent(context, WordsWidget.class);
        intent.setAction(WordsWidget.ACTION_WIDGET_REFRESH);
        WordsWidget.addIntentData(intent, widgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
      	      PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.updateButton, pendingIntent);
        
        Intent configIntent = new Intent(context, ConfigurationActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
		WordsWidget.addIntentData(configIntent, widgetId);
		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent,
      	      PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.settingsButton, configPendingIntent);
		
		AppWidgetManager.getInstance(context).updateAppWidget(widgetId, view);
        Log.d(TAG, "Updated common elements widgetId: " + widgetId);
        
        return view;
    }
    
    /**
     * Update the {@link PendingIntent} entities to add in the view
     * @param context
     * @param view
     * @param word
     * @param widgetId
     */
    protected void updateWordPendingIntents(Context context, RemoteViews view, Word word, int widgetId){
    	Intent switchIntent = new Intent(context,
				SwitchVisibilityService.class);
		switchIntent.putExtra(WordsWidget.WORD_DATA_KEY, word);
		WordsWidget.addIntentData(switchIntent, widgetId);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				switchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.widget, pendingIntent);

		Intent speechIntent = new Intent(context, SpeechService.class);
		speechIntent.putExtra(WordsWidget.WORD_DATA_KEY, word);
		WordsWidget.addIntentData(speechIntent, widgetId);
		PendingIntent speechPendingIntent = PendingIntent
				.getService(context, 0, speechIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.speechButton, speechPendingIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}