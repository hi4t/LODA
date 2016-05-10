package com.imba.library;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by zace on 2015/5/10.
 */
public class LodaService extends Service {

    private HashMap<String, LodaTask> tasks = new HashMap<>();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LodaEntry entry = (LodaEntry) intent.getSerializableExtra(Cons.DOWNLOAD_KEY);

        String action = intent.getStringExtra(Cons.DOWNLOAD_ACTION);

        doAction(action, entry);

        return super.onStartCommand(intent, flags, startId);
    }

    private void doAction(String action, LodaEntry entry) {

        switch (action) {

            case Cons.DOWNLOAD_ADD:
                startLoda(entry);
                break;
            case Cons.DOWNLOAD_PAUSE:
                pauseLoda(entry);
                break;
            case Cons.DOWNLOAD_RESUME:
                resumeLoda(entry);
                break;
            case Cons.DOWNLOAD_CANCEL:
                cancelLoda(entry);
                break;
        }
    }

    private void cancelLoda(LodaEntry entry) {
        LodaTask task = tasks.remove(entry.getId());
        if (task != null) {
            task.cancel();
        }
    }

    private void resumeLoda(LodaEntry entry) {
        startLoda(entry);
    }

    private void pauseLoda(LodaEntry entry) {
        LodaTask task = tasks.remove(entry.getId());

        if (task != null) {
            task.pause();
        }
    }

    private void startLoda(LodaEntry entry) {

        LodaTask task = new LodaTask(entry);
        task.start();
        tasks.put(entry.getId(), task);

    }
}
