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
import com.example.myvocab.database.VocabDBHelper;
import com.example.myvocab.database.VocabModel;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class StackView extends AppWidgetProvider {

    public static Intent serviceIntent;
    public static final String ACTION_TOAST ="actionToast";
    public static final String EXTRA_ITEM_POSITION="extraItemPosition";
    ArrayList<VocabModel> myWord;



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            serviceIntent = new Intent(context, StackViewService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            //Database initialization
            myWord =(ArrayList<VocabModel>) VocabDBHelper.getInstance(context).vocabDao().getAll();

            Log.d("StackView","This is onUpdate method of Stack View  "+myWord.get(1).getWord());
            updateAppWidget(context, appWidgetManager, appWidgetId, serviceIntent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Toast.makeText(context,"onEnabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Toast.makeText(context,"onDisabled",Toast.LENGTH_SHORT).show();
    }

    // Make this method public
    public  void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, Intent serviceIntent) {
        if (serviceIntent != null) { // Check if serviceIntent is not null
            // Construct the RemoteViews object
            Log.d("StackView","This is updateAppWidget method of Stack Activity "+myWord.get(1).getWord());
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stak_view);
            views.setTextViewText(R.id.stackViewEmptyTxt, "EMpty");
            views.setRemoteAdapter(R.id.stackView, serviceIntent);
            views.setEmptyView(R.id.stackView, R.id.stackViewEmptyTxt);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.stackView_Item_Txt, pendingIntent);
            Intent clickIntent=new Intent(context,StackView.class);
            clickIntent.setAction(ACTION_TOAST);
            PendingIntent clickPendingIntent=PendingIntent.getBroadcast(context,
                    0,clickIntent, PendingIntent.FLAG_IMMUTABLE);

            views.setPendingIntentTemplate(R.id.stackView,clickPendingIntent);

            //DataBase Fetching


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } else {
            // Log or handle the case when serviceIntent is null
            Toast.makeText(context, "serviceIntent is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_TOAST.equals(intent.getAction())) {
            int clickedPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, 0);

            TextToSpeech textToSpeech=new TextToSpeech();
            // Move the database query here
            myWord = (ArrayList<VocabModel>) VocabDBHelper.getInstance(context).vocabDao().getAll();

            if (myWord != null && clickedPosition < myWord.size()) {
                Log.d("StackView", "This is onReceive method");
//                Toast.makeText(context, "Position is " + myWord.get(clickedPosition).getWord(), Toast.LENGTH_SHORT).show();
                textToSpeech.position(clickedPosition, context.getApplicationContext());
            } else {
                Toast.makeText(context, "Invalid position or data not available", Toast.LENGTH_SHORT).show();
            }
        }
        super.onReceive(context, intent);
    }
}
