package com.example.myvocab.Utilites;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.myvocab.R;
import com.example.myvocab.database.VocabDBHelper;
import com.example.myvocab.database.VocabModel;

import java.util.ArrayList;

public class StackViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackViewItemFactory(getApplicationContext(), intent);
    }

    static class StackViewItemFactory implements RemoteViewsFactory {
        private Context context;
        private int appWidgetId;
        private ArrayList<VocabModel> myData;
        private VocabDBHelper database;

        public StackViewItemFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            this.database = VocabDBHelper.getInstance(context);
            this.myData = new ArrayList<>();
            updateData();
        }

        public void updateData() {
            myData.clear();
            myData.addAll(database.vocabDao().getAll());
            // Notify the widget to refresh
            AppWidgetManager.getInstance(context)
                    .notifyAppWidgetViewDataChanged(appWidgetId, R.id.stackView);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            // Typically called when underlying data changes
            updateData();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return myData.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stack_view_layout);
            remoteViews.setTextViewText(R.id.stackView_Item_Txt, myData.get(i).getWord());
            SystemClock.sleep(500);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
