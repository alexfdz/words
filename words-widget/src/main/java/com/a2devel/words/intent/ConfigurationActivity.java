package com.a2devel.words.intent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.a2devel.words.R;
import com.a2devel.words.widget.WordsWidget;

public class ConfigurationActivity extends PreferenceActivity {
    
	public static final String DICTIONARY_KEY = "dictionaryList";
	public static final String UPDATETIME_KEY = "updateList";
	
	protected static final String TAG = "ConfigurationActivity";
    private static final String PREFS_NAME = WordsWidget.class.getSimpleName();
    private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    
    
    
    
    

    public ConfigurationActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setResult(RESULT_CANCELED);
        
        // Find the widget id from the intent. 
        Intent intent = getIntent();
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
        		AppWidgetManager.INVALID_APPWIDGET_ID);
        
        Log.d(TAG, "ConfigurationActivity appWidgetId:" +widgetId);
        
        // If they gave us an intent without the widget id, just bail.
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        PreferenceManager localPrefs = getPreferenceManager();
        localPrefs.setSharedPreferencesName(ConfigurationActivity.getPreferencesName(widgetId));
        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.configuration);
        
        // Bind the action for the save button.
        findViewById(R.id.save_button).setOnClickListener(mOnClickListener);
        		
    }
    
    @Override
    public void onBackPressed() {
    	Intent active = new Intent(this, WordsWidget.class);
        active.setAction(WordsWidget.ACTION_WIDGET_REFRESH);
        WordsWidget.addIntentData(active, widgetId);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(this, 0, active, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(10*1000),
    			(long)10000,  actionPendingIntent);
    	Log.d(TAG, "Alarm refreshed:" +widgetId);
        this.startService(active);
        setResult(RESULT_OK, active);
        finish();
    }
    
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
        	onBackPressed();
        }
    };

    
    public static String getPreferencesName(int widgetId){
    	return PREFS_NAME + widgetId;
    }

}