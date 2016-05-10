package com.imba.library;

import android.content.Context;
import android.content.Intent;

/**
 * Created by zace on 2015/5/10.
 */
public class LodaManager {

    private static LodaManager manager;

    private LodaManager() {
    }

    public static synchronized LodaManager getInstance() {
        if (manager == null) {
            manager = new LodaManager();
        }

        return manager;
    }


    public void add(Context context, LodaEntry entry) {
        Intent intent = new Intent(context, LodaService.class);
        intent.putExtra(Cons.DOWNLOAD_KEY, entry);
        intent.putExtra(Cons.DOWNLOAD_ACTION, Cons.DOWNLOAD_ADD);
        context.startService(intent);
    }

    public void pause(LodaEntry entry) {

    }

    public void resume(LodaEntry entry) {

    }

    public void cancel(LodaEntry entry) {

    }


    public LodaManager addObserver(DataWatcher watcher) {
        DataChanger.getInstance().addObserver(watcher);
        return this;
    }

    public void removeObserver(DataWatcher watcher) {
        DataChanger.getInstance().deleteObserver(watcher);
    }

}
