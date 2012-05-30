package com.a2devel.words.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.service.UpdateService;

public class WordsWidget extends AppWidgetProvider {
	
	@Override
    public void onEnabled(Context context) {
    	RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_word);
    	
    	Intent updateServiceIntent = new Intent(context, UpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, updateServiceIntent,
        	      PendingIntent.FLAG_UPDATE_CURRENT);
    	view.setOnClickPendingIntent(R.id.updateButton, pendingIntent);
    }
	
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        context.startService(new Intent(context, UpdateService.class));
    }

}
