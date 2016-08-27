package com.moyinoluwa.gdgngevents.Widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.moyinoluwa.gdgngevents.DateParser;
import com.moyinoluwa.gdgngevents.ItemDetailActivity;
import com.moyinoluwa.gdgngevents.ItemDetailFragment;
import com.moyinoluwa.gdgngevents.ItemListActivity;
import com.moyinoluwa.gdgngevents.R;

/**
 * IntentService which handles updating the Events widgets with the latest data
 */
public class GdgNgEventsIntentService extends IntentService {
    private SharedPreferences sharedPreferences;

    public GdgNgEventsIntentService() {
        super("GdgNgEventsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the event widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                GdgNgEventsWidgetProvider.class));

        // Get the event's data from Shared preference
        sharedPreferences = getSharedPreferences("gdg_events", Context.MODE_PRIVATE);

        String widgetName = sharedPreferences.getString("widget_name", "Name");
        String widgetShortDesc = sharedPreferences.getString("widget_shortDesc", "ShortDescription");
        String widgetLongDesc = sharedPreferences.getString("widget_longDesc", "LongDescription");
        String widgetVenue = sharedPreferences.getString("widget_venue", "Venue");
        String widgetDate = sharedPreferences.getString("widget_date", "Date");
        String widgetTime = sharedPreferences.getString("widget_time", "Time");
        String widgetDuration = sharedPreferences.getString("widget_duration", "Duration");
        String widgetSpeakers = sharedPreferences.getString("widget_speakers", "Speakers");
        String widgetTags = sharedPreferences.getString("widget_tags", "Tags");
        String widgetGdg = sharedPreferences.getString("widget_gdg", "GDG");

        // Perform this loop procedure for each event widget
        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.gdg_ng_events_widget;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setTextViewText(R.id.event_name, widgetName);
            views.setTextViewText(R.id.short_description, widgetShortDesc);
            views.setTextViewText(R.id.venue, widgetVenue);
            views.setTextViewText(R.id.event_date, widgetDate);
            views.setTextViewText(R.id.event_time, DateParser.formattedTime(widgetTime));
            views.setTextViewText(R.id.duration, widgetDuration);

            // Create an Intent to launch ItemListActivity
            Intent launchIntent = new Intent(this, ItemDetailActivity.class);

            launchIntent.putExtra(ItemDetailFragment.EVENT_NAME, widgetName);
            launchIntent.putExtra(ItemDetailFragment.SHORT_DESCRIPTION, widgetShortDesc);
            launchIntent.putExtra(ItemDetailFragment.LONG_DESCRIPTION, widgetLongDesc);
            launchIntent.putExtra(ItemDetailFragment.VENUE, widgetVenue);
            launchIntent.putExtra(ItemDetailFragment.EVENT_DATE, widgetDate);
            launchIntent.putExtra(ItemDetailFragment.EVENT_TIME, widgetTime);
            launchIntent.putExtra(ItemDetailFragment.DURATION, widgetDuration);
            launchIntent.putExtra(ItemDetailFragment.SPEAKERS, widgetSpeakers);
            launchIntent.putExtra(ItemDetailFragment.EVENT_TAGS, widgetTags);
            launchIntent.putExtra(ItemDetailFragment.EVENT_GDG, widgetGdg);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
