package com.a2devel.words.service;

import java.io.IOException;

import mt.rcasha.dict.client.DictException;
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
import com.a2devel.words.dao.Dictionary;
import com.a2devel.words.to.Word;
import com.a2devel.words.widget.WordsWidget;

public class UpdateService extends Service {

	private static final String TAG = "SwitchVisibilityService";
	
	private Dictionary dictionary = null;
	
    @Override
    public void onStart(Intent intent, int startId) {
    	Log.d(TAG, "Started service");
        try {
			dictionary = new Dictionary("eng-spa");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        RemoteViews view = updateView(this);
        ComponentName widget = new ComponentName(this, WordsWidget.class);
        AppWidgetManager.getInstance(this).updateAppWidget(widget, view);
    }

    /**
     * @param context
     * @return
     */
    public RemoteViews updateView(Context context) {
        RemoteViews view = null;
        Word word = null;
        try {
			word = dictionary.getWord();
		} catch (Exception e) {
			e.printStackTrace(); //TODO log
			word = null;
		} 
        
        if (word != null) {
        	Log.d(TAG, "Correct word " +word.getWord() + "::"+word.getTranslation());
            view = new RemoteViews(context.getPackageName(), R.layout.widget_word);

            view.setTextViewText(R.id.word, word.getWord());
            view.setTextViewText(R.id.translation, word.getTranslation());

            Intent switchIntent = new Intent(context, SwitchVisibilityService.class);
            switchIntent.putExtra(SwitchVisibilityService.WORD_VISIBLE_KEY, true);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, switchIntent,
            	      PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.widget, pendingIntent);
            
        } else {
            view = new RemoteViews(context.getPackageName(), R.layout.widget_message);
            CharSequence errorMessage = context.getText(R.string.widget_error);
            view.setTextViewText(R.id.message, errorMessage);
        }
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