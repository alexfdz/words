package com.a2devel.words.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mt.rcasha.dict.client.DictException;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.dao.Dictionary;
import com.a2devel.words.intent.ConfigurationActivity;
import com.a2devel.words.to.Word;

/**
 * Service to update a new random {@link Word} entity in the widget.
 * Manages the {@link Dictionary} access object and handles its exceptions.
 * 
 * @author alex
 */
public class UpdateService extends WordsService {

	private static final String TAG = "UpdateService";
	/**
	 * Max buffer size to control repeated words 
	 */
	private static final int WORDS_BUFFER_SIZE = 50;
	/**
	 * Buffer to control repeated words 
	 */
	private List<String> wordsBuffer;
	private Dictionary dictionary;
	
	@Override
	public void onCreate() {
		super.onCreate();
		wordsBuffer = new ArrayList<String>();
	}
	
    @Override
    public RemoteViews updateView(Context context, int widgetId) {
    	Word word = null;   
    	CharSequence errorMessage = null;
    	
    	Log.d(TAG, "Started UpdateService widgetId:"+widgetId);
    	
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_word);
        view.setTextViewText(R.id.word,context.getText(R.string.widget_loading));
        view.setViewVisibility(R.id.translation, View.INVISIBLE);
        view.setViewVisibility(R.id.word, View.VISIBLE);
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, view);
        
        Log.d(TAG, "updated textview widgetId: " + widgetId);
        
        try {
			word = getWord(context, widgetId);
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
            word.setWordVisible(true);
            this.updateWordPendingIntents(context, view, word, widgetId);
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
     * For a given widget, gets the next random {@link Word} entity
     * @return
     */
    private Word getWord(Context context, int widgetId) throws DictException, IOException{
    	SharedPreferences preferences = context.getSharedPreferences(ConfigurationActivity.getPreferencesName(widgetId), Context.MODE_PRIVATE);
    	return getWord(preferences.getString(context.getText(R.string.pref_dictionary_key).toString(), null));
    }
    
    /**
     * For a given database id gets the next random {@link Word} entity
     * @param database
     * @return
     * @throws DictException
     * @throws IOException
     */
    private Word getWord(String database) throws DictException, IOException{
    	Dictionary dictionary =  getDictionary();
    	Word word = null;
    	if(dictionary != null && database != null){
    		word = getDictionary().getWord(database);
        	if(word != null){
        		if(wordsBuffer.contains(word.getWord())){
            		word = getWord(database);
            	}else{
            		wordsBuffer.add(word.getWord());
            		if(wordsBuffer.size() > WORDS_BUFFER_SIZE){
            			wordsBuffer.remove(0);
            		}
            	}
        	}
    	}
    	return word;
    }
    
    /**
     * Get the {@link Dictionary} entity. Creates a new one if 
     * doesn't exist
     * @return
     * @throws DictException 
     * @throws IOException 
     */
    private Dictionary getDictionary() throws DictException, IOException{
    	if(dictionary == null){
    		try {
    			dictionary = new Dictionary();
    		} catch (DictException e) {
    			Log.e(TAG, e.getMessage(), e);
    			throw e;
    		} catch (IOException e) {
    			Log.e(TAG, e.getMessage(), e);
    			throw e;
    		}
    	}
    	return dictionary;
    }

	@Override
	public void onDestroy() {
		try {
			getDictionary().stop();
		} catch (DictException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		super.onDestroy();
	}

}