package com.a2devel.words.intent;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.EditText;

import com.a2devel.words.R;

public class ConfigurationActivity extends PreferenceActivity {
    static final String TAG = "ConfigurationActivity";

    private static final String PREFS_NAME = "com.a2devel.words.WordsWidget";
    private static final String PREF_PREFIX_KEY = "prefix_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetPrefix;

    public ConfigurationActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setResult(RESULT_CANCELED);
        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.configuration);
        
        // Bind the action for the save button.
        findViewById(R.id.save_button).setOnClickListener(mOnClickListener);
        		
        // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
            		AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

    }
    
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = ConfigurationActivity.this;
            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

}