package com.a2devel.words.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.widget.WordsWidget;

public class SwitchVisibilityService extends Service {
	
	private static final String TAG = "SwitchVisibilityService";
	public static final String WORD_VISIBLE_KEY = "com.a2devel.words.word_visible";
	
    @Override
    public void onStart(Intent intent, int startId) {
    	Log.d(TAG, "Started service");
        boolean isWordVisible = intent.getExtras().getBoolean(WORD_VISIBLE_KEY);
        RemoteViews view = updateView(this, isWordVisible);
        
        ComponentName widget = new ComponentName(this, WordsWidget.class);
        AppWidgetManager.getInstance(this).updateAppWidget(widget, view);
    }

    /**
     * @param context
     * @return
     */
    public RemoteViews updateView(Context context, boolean isWordVisible) {
    	Log.d(TAG, "isWordVisible = "+isWordVisible);
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_word);
        if(isWordVisible){
        	view.setViewVisibility(R.id.word, View.INVISIBLE);
            view.setViewVisibility(R.id.translation, View.VISIBLE);
        }else{
        	view.setViewVisibility(R.id.word, View.VISIBLE);
            view.setViewVisibility(R.id.translation, View.INVISIBLE);
        }
        
        Intent switchIntent = new Intent(context, SwitchVisibilityService.class);
        switchIntent.putExtra(SwitchVisibilityService.WORD_VISIBLE_KEY, !isWordVisible);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, switchIntent,
        	      PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.widget, pendingIntent);

        Intent active = new Intent(context, WordsWidget.class);
        active.setAction(WordsWidget.ACTION_WIDGET_REFRESH);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        view.setOnClickPendingIntent(R.id.updateButton, actionPendingIntent);
        return view;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}