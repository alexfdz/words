package com.a2devel.words.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.widget.WordsWidget;

public abstract class WordsService extends Service {

	private static final String TAG = "WordsService";
	
    @Override
    public void onStart(Intent intent, int startId) {
        RemoteViews view = updateView(this);
        updateCommonElements(this, view);
        ComponentName widget = new ComponentName(this, WordsWidget.class);
        AppWidgetManager.getInstance(this).updateAppWidget(widget, view);
    }

    public abstract RemoteViews updateView(Context context);
    
    /**
     * @param context
     * @return
     */
    protected RemoteViews updateCommonElements(Context context, RemoteViews view) {
        // Update button actions
        Intent active = new Intent(context, WordsWidget.class);
        active.setAction(WordsWidget.ACTION_WIDGET_REFRESH);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
        view.setOnClickPendingIntent(R.id.updateButton, actionPendingIntent);
        
//        active = new Intent(context, WordsWidget.class);
//        active.setAction(WordsWidget.ACTION_WIDGET_SETTINGS);
//        actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
//        view.setOnClickPendingIntent(R.id.updateButton, actionPendingIntent);
        
        Log.d(TAG, "Updated common elements");
        
        return view;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}