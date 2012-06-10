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

/**
 * TODO: Pasarlo a Activity
 * @author alex
 *
 */
public class SpeechService extends Service {

	private static final String TAG = "SpeechService";
	public static final String WORD_KEY = "com.a2devel.words.word";
	
	private TextToSpeech speech;
	private boolean ready = false;
	
	@Override
	public void onCreate() {
		Log.d(TAG, "SpeechService onCreate");
		super.onCreate();
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
	
    @Override
    public void onStart(Intent intent, int startId) {
    	Log.d(TAG, "SpeechService onStart");
    	if(intent != null && ready){
    		Word word = null;
    		boolean isWordVisible = intent.getBooleanExtra(SwitchVisibilityService.WORD_VISIBLE_KEY, false);
    		if(intent.getSerializableExtra(SpeechService.WORD_KEY) instanceof Word){
    			word = (Word)intent.getSerializableExtra(SpeechService.WORD_KEY);
    		}
    		this.speech(word, isWordVisible);
    	}else{
    		Toast.makeText(this, this.getText(R.string.speech_starting),
					Toast.LENGTH_SHORT).show();
    	}
    }
    
    /**
     * @param word
     * @param isWordVisible
     */
    private void speech(Word word, boolean isWordVisible){
    	if(word != null){
			String text = this.getTextToSpeech(word, isWordVisible);
			String language = this.getLanguage(word, isWordVisible);
			
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
     * @param word
     * @param isWordVisible
     * @return
     */
    private String getTextToSpeech(Word word, boolean isWordVisible){
    	String text = null;
    	if(word != null){
    		if(isWordVisible){
    			text = word.getWord();
    		}else{
    			text = word.getTranslation();
    		}
    	}
    	return text;
    }
    
    /**
     * @param word
     * @param isWordVisible
     * @return
     */
    private String getLanguage(Word word, boolean isWordVisible){
    	String language = null;
    	if(word != null){
    		if(isWordVisible){
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