package com.example.myvocab;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.myvocab.R;

public class StackViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackViewItemFactory(getApplicationContext(),intent);
    }
    static class StackViewItemFactory implements RemoteViewsFactory{
        private Context context;
        private int appWidgetId;
        private String[] myData={"One","Two","Three"};

        public StackViewItemFactory(Context context,Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return myData.length;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.stack_view_layout);
            remoteViews.setTextViewText(R.id.stackView_Item_Txt,myData[i]);
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
