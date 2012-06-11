package com.a2devel.words.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.to.Word;
import com.a2devel.words.widget.WordsWidget;

/**
 * Service to manage the visibility of the word and its translation 
 * @author alex
 * @see WordsService
 */
public class SwitchVisibilityService extends WordsService {
	
	private static final String TAG = "SwitchVisibilityService";
	private Word word;
	
	
    @Override
	public void onStart(Intent intent, int startId) {
		if(intent != null && intent.getSerializableExtra(WordsWidget.WORD_DATA_KEY) instanceof Word){
			word = (Word)intent.getSerializableExtra(WordsWidget.WORD_DATA_KEY);
		}else{
			word = null;
		}
		super.onStart(intent, startId);
	}

    /**
     * Updates the visibility of the view depending on if the word is visible or 
     * hidden.
     * 
     * @param context
     * @return
     */
    public RemoteViews updateView(Context context, int widgetId) {
    	Log.d(TAG, "Started SwitchVisibilityService widgetId:" + widgetId);
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_word);
		if (word != null) {
			if (word.isWordVisible()) {
				view.setViewVisibility(R.id.word, View.INVISIBLE);
				view.setViewVisibility(R.id.translation, View.VISIBLE);
			} else {
				view.setViewVisibility(R.id.word, View.VISIBLE);
				view.setViewVisibility(R.id.translation, View.INVISIBLE);
			}
			word.setWordVisible(!word.isWordVisible());
			this.updateWordPendingIntents(context, view, word, widgetId);
		} else {
            view.setTextViewText(R.id.word, context.getText(R.string.widget_error));
            view.setTextViewText(R.id.translation, "");
        }
       
        return view;
    }

}