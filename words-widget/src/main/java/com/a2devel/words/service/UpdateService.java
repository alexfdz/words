package com.a2devel.words.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mt.rcasha.dict.client.DictException;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.dao.Dictionary;
import com.a2devel.words.to.Word;

public class UpdateService extends WordsService {

	private static final String TAG = "SwitchVisibilityService";
	private static final int WORDS_BUFFER_SIZE = 50;
	
	private List<String> wordsBuffer = new ArrayList<String>();
	
    /**
     * @param context
     * @return
     */
    public RemoteViews updateView(Context context, int widgetId) {
    	Word word = null;   
    	CharSequence errorMessage = null;
    	
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_word);
        view.setTextViewText(R.id.word,context.getText(R.string.widget_loading));
        view.setViewVisibility(R.id.translation, View.INVISIBLE);
        view.setViewVisibility(R.id.word, View.VISIBLE);
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, view);
        
        try {
			word = getWord();
		} catch (DictException e) {
			errorMessage = context.getText(R.string.widget_error_dict);
			Log.e(TAG, errorMessage.toString(), e);
		} catch (IOException e) {
			errorMessage = context.getText(R.string.widget_error_io);
			Log.e(TAG, errorMessage.toString(), e);
		}
        
        if (word != null) {
        	Log.d(TAG, "Correct word " +word.getWord() + "::"+word.getTranslation());
            view.setTextViewText(R.id.word, word.getWord());
            view.setTextViewText(R.id.translation, word.getTranslation());

            Intent switchIntent = new Intent(context, SwitchVisibilityService.class);
            switchIntent.putExtra(SwitchVisibilityService.WORD_VISIBLE_KEY, true);
            switchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, switchIntent,
            	      PendingIntent.FLAG_UPDATE_CURRENT);
            view.setOnClickPendingIntent(R.id.widget, pendingIntent);
            
        } else {
        	if(errorMessage == null){
        		errorMessage = context.getText(R.string.widget_error);
        	}
            view.setTextViewText(R.id.word, errorMessage);
            view.setTextViewText(R.id.translation, "");
        }
        
        return view;
    }
    
    
    /**
     * @return
     */
    private Word getWord() throws DictException, IOException{
    	Word word = getDictionary().getWord();
    	if(word != null){
    		if(wordsBuffer.contains(word.getWord())){
        		word = getWord();
        	}else{
        		wordsBuffer.add(word.getWord());
        		if(wordsBuffer.size() > WORDS_BUFFER_SIZE){
        			wordsBuffer.remove(0);
        		}
        	}
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