package com.a2devel.words.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.a2devel.words.R;

public class SwitchVisibilityService extends WordsService {
	
	private static final String TAG = "SwitchVisibilityService";
	public static final String WORD_VISIBLE_KEY = "com.a2devel.words.word_visible";
	private boolean isWordVisible;
	
	
    @Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "Started SwitchVisibilityService");
		isWordVisible = false;
		if(intent.getExtras() != null){
			isWordVisible = intent.getExtras().getBoolean(WORD_VISIBLE_KEY);
		}
		super.onStart(intent, startId);
	}

    /**
     * @param context
     * @return
     */
    public RemoteViews updateView(Context context) {
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
      
        return view;
    }

}