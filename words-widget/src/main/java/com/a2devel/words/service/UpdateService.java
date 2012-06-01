package com.a2devel.words.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mt.rcasha.dict.client.DictException;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.dao.Dictionary;
import com.a2devel.words.to.Word;
import com.a2devel.words.widget.WordsWidget;

public class UpdateService extends WordsService {

	private static final String TAG = "SwitchVisibilityService";
	private List<String> words = new ArrayList<String>();
	
    /**
     * @param context
     * @return
     */
    public RemoteViews updateView(Context context) {
    	Log.d(TAG, "Current instance " +this.toString());
    	
    	ComponentName widget = new ComponentName(this, WordsWidget.class);
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_word);
        view.setTextViewText(R.id.word,context.getText(R.string.widget_loading));
        view.setViewVisibility(R.id.translation, View.INVISIBLE);
        view.setViewVisibility(R.id.word, View.VISIBLE);
        AppWidgetManager.getInstance(this).updateAppWidget(widget, view);
        
        Word word = getWord();
        
        if (word != null) {
        	Log.d(TAG, "Correct word " +word.getWord() + "::"+word.getTranslation());
            view.setTextViewText(R.id.word, word.getWord());
            view.setTextViewText(R.id.translation, word.getTranslation());

            Intent switchIntent = new Intent(context, SwitchVisibilityService.class);
            switchIntent.putExtra(SwitchVisibilityService.WORD_VISIBLE_KEY, true);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, switchIntent,
            	      PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.widget, pendingIntent);
            
        } else {
            view.setTextViewText(R.id.word, context.getText(R.string.widget_error));
            view.setTextViewText(R.id.translation, "");
        }
        
        AppWidgetManager.getInstance(this).updateAppWidget(widget, view);
        return view;
    }
    
    
    /**
     * @return
     */
    private Word getWord(){
    	Word word = null;
    	try {
			word = getDictionary().getWord();
		} catch (Exception e) {
			e.printStackTrace(); //TODO log
			word = null;
		} 
    	if(word != null && words.contains(word.getWord())){
    		word = getWord();
    	}else{
    		words.add(word.getWord());
    	}
    	return word;
    }
    
    /**
     * @return
     */
    private Dictionary getDictionary(){
    	Dictionary dictionary = null;
    	
    	try {
 			dictionary = new Dictionary("eng-spa");
 		} catch (IOException e) {
 			Log.e(TAG, e.getMessage(), e); 			
 		} catch (DictException e) {
 			Log.e(TAG, e.getMessage(), e);
 		}
    	 
    	return dictionary;
    }

}