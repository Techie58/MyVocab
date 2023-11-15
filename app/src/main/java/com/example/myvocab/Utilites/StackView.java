package com.example.myvocab.Utilites;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.myvocab.MainActivity;
import com.example.myvocab.R;

/**
 * Implementation of App Widget functionality.
 */
public class StackView extends AppWidgetProvider {

    public static Intent serviceIntent;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            serviceIntent = new Intent(context, StackViewService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            Log.d("StackView","This is onUpdate method of Stack View");
            updateAppWidget(context, appWidgetManager, appWidgetId, serviceIntent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // Make this method public
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, Intent serviceIntent) {
        if (serviceIntent != null) { // Check if serviceIntent is not null
            // Construct the RemoteViews object
            Log.d("StackView","This is updateAppWidget method of Stack Activity");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stak_view);
            views.setTextViewText(R.id.stackViewEmptyTxt, "EMpty");
            views.setRemoteAdapter(R.id.stackView, serviceIntent);
            views.setEmptyView(R.id.stackView, R.id.stackViewEmptyTxt);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.stackView_Item_Txt, pendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } else {
            // Log or handle the case when serviceIntent is null
            Toast.makeText(context, "serviceIntent is null", Toast.LENGTH_SHORT).show();
        }
    }
}
