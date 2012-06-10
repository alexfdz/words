package com.a2devel.words.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.to.Word;
import com.a2devel.words.widget.WordsWidget;

public class SwitchVisibilityService extends WordsService {
	
	private static final String TAG = "SwitchVisibilityService";
	public static final String WORD_VISIBLE_KEY = "com.a2devel.words.word_visible";
	private boolean isWordVisible;
	private Word word;
	
	
    @Override
	public void onStart(Intent intent, int startId) {
		isWordVisible = intent.getBooleanExtra(WORD_VISIBLE_KEY, false);
		
		if(intent.getSerializableExtra(SpeechService.WORD_KEY) instanceof Word){
			word = (Word)intent.getSerializableExtra(SpeechService.WORD_KEY);
		}else{
			word = null;
		}
		
		super.onStart(intent, startId);
	}

    /**
     * @param context
     * @return
     */
    public RemoteViews updateView(Context context, int widgetId) {
    	Log.d(TAG, "Started SwitchVisibilityService widgetId:"+widgetId);
    	
    	
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
        switchIntent.putExtra(SpeechService.WORD_KEY, word);
        WordsWidget.addIntentData(switchIntent, widgetId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, switchIntent,
        	      PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.widget, pendingIntent);
        
        Intent speechIntent = new Intent(context, SpeechService.class);
        speechIntent.putExtra(SpeechService.WORD_KEY, word);
        speechIntent.putExtra(SwitchVisibilityService.WORD_VISIBLE_KEY, !isWordVisible);
        WordsWidget.addIntentData(speechIntent, widgetId);
        PendingIntent speechPendingIntent = PendingIntent.getService(context, 0, speechIntent,
        		PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.speechButton, speechPendingIntent);
      
        return view;
    }

}