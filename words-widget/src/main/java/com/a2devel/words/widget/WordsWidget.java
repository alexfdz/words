package com.a2devel.words.widget;

import java.io.IOException;

import mt.rcasha.dict.client.DictException;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.a2devel.words.R;
import com.a2devel.words.dao.Dictionary;

public class WordsWidget extends AppWidgetProvider {
	
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
    	
        // To prevent any ANR timeouts, we perform the update in a service
        context.startService(new Intent(context, UpdateService.class));
    }

    public static class UpdateService extends Service {

    	private Dictionary dictionary = null;
    	
        @Override
        public void onStart(Intent intent, int startId) {
            RemoteViews view = updateView(this);
            
            try {
    			dictionary = new Dictionary("eng-esp");
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (DictException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

            ComponentName widget = new ComponentName(this, WordsWidget.class);
            AppWidgetManager.getInstance(this).updateAppWidget(widget, view);
        }

        /**
         * @param context
         * @return
         */
        public RemoteViews updateView(Context context) {
            RemoteViews view = null;
            String word = null;
            try {
				word = dictionary.getWord();
			} catch (Exception e) {
				e.printStackTrace(); //TODO log
			} 

            if (word != null) {
                // Build an update that holds the updated widget contents
                view = new RemoteViews(context.getPackageName(), R.layout.widget_word);

                view.setTextViewText(R.id.word, word);

                // TODO Onclick action 
                Intent defineIntent = new Intent(Intent.ACTION_VIEW);
                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        0 /* no requestCode */, defineIntent, 0 /* no flags */);
                view.setOnClickPendingIntent(R.id.widget, pendingIntent);

            } else {
                view = new RemoteViews(context.getPackageName(), R.layout.widget_message);
                CharSequence errorMessage = context.getText(R.string.widget_error);
                view.setTextViewText(R.id.message, errorMessage);
            }
            return view;
        }

        @Override
        public IBinder onBind(Intent intent) {
            // We don't need to bind to this service
            return null;
        }
    }
}
