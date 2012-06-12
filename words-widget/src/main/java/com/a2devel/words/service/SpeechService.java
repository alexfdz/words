package com.a2devel.words.service;

import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.a2devel.words.R;
import com.a2devel.words.to.Word;
import com.a2devel.words.widget.WordsWidget;

/**
 * TexToSpeech service integration to reproduce the word/translation
 * @author alex
 */
public class SpeechService extends Service {

	private static final String TAG = "SpeechService";
	private TextToSpeech speech;
	/**
	 * Is the service ready indicator 
	 */
	private boolean ready = false;
	
	@Override
    public void onStart(Intent intent, int startId) {
    	Log.d(TAG, "SpeechService onStart");
    	if(intent != null && isReady()){
    		Word word = null;
    		if(intent.getSerializableExtra(WordsWidget.WORD_DATA_KEY) instanceof Word){
    			word = (Word)intent.getSerializableExtra(WordsWidget.WORD_DATA_KEY);
    		}
    		this.speech(word);
    	}else{
    		Toast.makeText(this, this.getText(R.string.speech_starting),
					Toast.LENGTH_SHORT).show();
    	}
    }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if(speech != null){
			speech.stop();
			ready = false;
		}
		super.onDestroy();
	}
    
	/**
	 * Checks if the textToSpeach service is ready, initialize it otherwise
	 * @return the ready
	 */
	public boolean isReady() {
		if(speech == null){
    		ready = false;
    		speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
    			@Override
    			public void onInit(int status) {
    				ready = true;
    				Log.d(TAG, "TextToSpeech init");
    				Toast.makeText(SpeechService.this, SpeechService.this.getText(R.string.speech_initialized),
    						Toast.LENGTH_SHORT).show();
    			}
    		});
    	}
		return ready;
	}
	
    /**
     * Execute the speech integration for the given {@link Word} entity.
     * Resolves if the text to speech is the word or its translation.
     * @param word
     * @param isWordVisible
     */
    private void speech(Word word){
    	if(word != null){
			String text = this.getTextToSpeech(word);
			String language = this.getLanguage(word);
			
			if(text != null && language != null){
				Log.d(TAG, "Speech word: " + text + " lang: "+language);
				Locale locale = new Locale(language);
				int languageAvailable = speech.isLanguageAvailable(locale);
				if(languageAvailable != TextToSpeech.LANG_NOT_SUPPORTED &&
						languageAvailable != TextToSpeech.LANG_MISSING_DATA){
					speech.setLanguage(locale);
					speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
				}else{
					Log.d(TAG, "Lang: "+language+" not supported.");
					Toast.makeText(this, this.getText(R.string.speech_lang_notsupported),
							Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, this.getText(R.string.speech_error),
						Toast.LENGTH_SHORT).show();
			}
		}
    }
    
    
    /**
     * For a given {@link Word} entity resolves the text to speech
     * depending on the visibility of the word.
     * @param word
     * @param isWordVisible
     * @return
     */
    private String getTextToSpeech(Word word){
    	String text = null;
    	if(word != null){
    		if(word.isWordVisible()){
    			text = word.getWord();
    		}else{
    			text = word.getTranslation();
    		}
    	}
    	return text;
    }
    
    /**
     * For a given {@link Word} entity resolves the language
     * of the text to speech depending on the visibility of the word.
     * 
     * @param word
     * @param isWordVisible
     * @return
     */
    private String getLanguage(Word word){
    	String language = null;
    	if(word != null){
    		if(word.isWordVisible()){
    			language = word.getWordLanguage();
    		}else{
    			language = word.getTranslationLanguage();
    		}
    	}
    	return language;
    }
    

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}