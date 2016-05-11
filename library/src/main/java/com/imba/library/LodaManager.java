package com.imba.library;

import android.content.Context;
import android.content.Intent;

/**
 * Created by zace on 2015/5/10.
 */
public class LodaManager {

    private static LodaManager manager;
    private final Context context;

    private LodaManager(Context context) {
        this.context = context;
    }

    public static synchronized LodaManager getInstance(Context context) {
        if (manager == null) {
            manager = new LodaManager(context);
        }

        return manager;
    }


    public void add(LodaEntry entry) {
        Intent intent = new Intent(context, LodaService.class);
        intent.putExtra(Cons.DOWNLOAD_KEY, entry);
        intent.putExtra(Cons.DOWNLOAD_ACTION, Cons.DOWNLOAD_ADD);
        context.startService(intent);
    }

    public void pause(LodaEntry entry) {
        Intent intent = new Intent(context, LodaService.class);
        intent.putExtra(Cons.DOWNLOAD_KEY, entry);
        intent.putExtra(Cons.DOWNLOAD_ACTION, Cons.DOWNLOAD_PAUSE);
        context.startService(intent);
    }

    public void resume(LodaEntry entry) {
        Intent intent = new Intent(context, LodaService.class);
        intent.putExtra(Cons.DOWNLOAD_KEY, entry);
        intent.putExtra(Cons.DOWNLOAD_ACTION, Cons.DOWNLOAD_RESUME);
        context.startService(intent);

    }

    public void cancel(LodaEntry entry) {
        Intent intent = new Intent(context, LodaService.class);
        intent.putExtra(Cons.DOWNLOAD_KEY, entry);
        intent.putExtra(Cons.DOWNLOAD_ACTION, Cons.DOWNLOAD_CANCEL);
        context.startService(intent);

    }

    public void pauseAll() {
        Intent intent = new Intent(context, LodaService.class);
        intent.putExtra(Cons.DOWNLOAD_ACTION, Cons.DOWNLOAD_PAUSE_ALL);
        context.startService(intent);
    }

    public void recoverAll() {
        Intent intent = new Intent(context, LodaService.class);
        intent.putExtra(Cons.DOWNLOAD_ACTION, Cons.DOWNLOAD_RECOVER_ALL);
        context.startService(intent);
    }


    public LodaManager addObserver(DataWatcher watcher) {
        DataChanger.getInstance().addObserver(watcher);
        return this;
    }

    public void removeObserver(DataWatcher watcher) {
        DataChanger.getInstance().deleteObserver(watcher);
    }

}
