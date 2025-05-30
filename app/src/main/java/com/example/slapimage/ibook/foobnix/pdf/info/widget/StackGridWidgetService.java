package com.example.slapimage.ibook.foobnix.pdf.info.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.slapimage.ibook.foobnix.dao2.FileMeta;
import com.example.slapimage.ibook.foobnix.model.AppData;
import com.example.slapimage.ibook.foobnix.model.AppProfile;
import com.example.slapimage.ibook.foobnix.model.AppState;
import com.example.slapimage.ibook.foobnix.pdf.info.IMG;
import com.example.slapimage.R;
import com.example.slapimage.ibook.foobnix.sys.ImageExtractor;
import com.example.slapimage.ibook.foobnix.ui2.AppDB;
import com.example.slapimage.musicplayer.ApplicationClass;

//import com.example.slapimage.ibook.foobnix.LibreraApp;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StackGridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(getApplicationContext(), intent);
    }

}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<FileMeta> recent;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        AppProfile.init(context);
        onDataSetChanged();
    }

    @Override
    public void onDataSetChanged() {
        if (AppState.get().isStarsInWidget) {
            recent = AppData.get().getAllFavoriteFiles(false);
        } else {
            recent = AppData.get().getAllRecent(false);
        }
        AppDB.removeClouds(recent);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recent.size() > AppState.get().widgetItemsCount ? AppState.get().widgetItemsCount : recent.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.widget_grid_image);

        if (recent.size() <= position) {
            return v;
        }

        FileMeta uri = recent.get(position);

        String url = IMG.toUrl(uri.getPath(), ImageExtractor.COVER_PAGE_WITH_EFFECT, IMG.getImageSize());

        try {
            Bitmap image = Glide.with(ApplicationClass.context).asBitmap().load(url).submit().get();
            v.setImageViewBitmap(R.id.imageView1, image);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Bundle extras = new Bundle();
        extras.putParcelable("uri", Uri.fromFile(new File(uri.getPath())));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        v.setOnClickFillInIntent(R.id.imageView1, fillInIntent);

        return v;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
