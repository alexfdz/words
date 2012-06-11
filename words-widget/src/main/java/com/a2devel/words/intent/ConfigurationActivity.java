package com.a2devel.words.intent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.a2devel.words.R;
import com.a2devel.words.widget.WordsWidget;

/**
 * Widget {@link PreferenceActivity} to manage the behaviour properties like dictionary
 * to use and automatic update interval.
 * 
 * Starts the widget update action on close/back/save actions.
 *  
 * @author fernanda
 *
 */
public class ConfigurationActivity extends PreferenceActivity {
    
	protected static final String TAG = "ConfigurationActivity";
    
	/**
	 * Prefix for preferences name
	 */
	private static final String PREFS_NAME = WordsWidget.class.getSimpleName();
    /**
     * Application widget id
     */
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
        
        SharedPreferences preferences = this.getSharedPreferences(ConfigurationActivity.getPreferencesName(widgetId), 
        		Context.MODE_PRIVATE);
        String interval = preferences.getString(this.getText(R.string.pref_updateTime_key).toString(), null);
        
        if(interval != null){
        	long updateTime = Long.parseLong(interval);
        	PendingIntent actionPendingIntent = PendingIntent.getBroadcast(this, 0, active, 0);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
            		updateTime,  actionPendingIntent);
        	Log.d(TAG, "Alarm refreshed:" +widgetId);
        }
        startService(active);
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