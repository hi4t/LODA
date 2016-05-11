package com.imba.library;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zace on 2015/5/10.
 */
public class LodaService extends Service {

    private HashMap<String, LodaTask> tasks = new HashMap<>();
    private ExecutorService executors;
    private LinkedBlockingQueue<LodaEntry> queue = new LinkedBlockingQueue<>();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LodaEntry entry = (LodaEntry) msg.obj;
            switch (entry.getStatus()) {
                case COMPLETE:
                    tasks.remove(entry);
                case PAUSE:
                case CANCEL:
                    entry = queue.poll();
                    if (entry != null) {
                        startLoda(entry);
                    }
            }

            DataChanger.getInstance().postStatus((LodaEntry) msg.obj);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        executors = Executors.newCachedThreadPool();
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
                addLoda(entry);
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

            case Cons.DOWNLOAD_PAUSE_ALL:
                pauseAll();

                break;
            case Cons.DOWNLOAD_RECOVER_ALL:
                recoverAll();
                break;
        }
    }

    private void recoverAll() {
        ArrayList<LodaEntry> entrys = DataChanger.getInstance().getAllPauseEntry();

        if (entrys != null && entrys.size() > 0) {
            for (LodaEntry entry : entrys) {
                addLoda(entry);
            }
        }
    }

    private void pauseAll() {

        while (queue.iterator().hasNext()) {
            LodaEntry entry = queue.poll();
            entry.setStatus(LodaEntry.STATUS.PAUSE);
            DataChanger.getInstance().postStatus(entry);
        }

        for (Map.Entry<String, LodaTask> entry : tasks.entrySet()) {
            entry.getValue().pause();
        }

        tasks.clear();
    }


    private void addLoda(LodaEntry entry) {
        if (tasks.size() >= Cons.MAX_CACHE) {
            queue.offer(entry);
            entry.setStatus(LodaEntry.STATUS.WAITING);
            DataChanger.getInstance().postStatus(entry);
        } else {
            startLoda(entry);
        }
    }


    private void cancelLoda(LodaEntry entry) {
        LodaTask task = tasks.remove(entry.getId());
        if (task != null) {
            task.cancel();
        } else {
            queue.remove(entry);
            entry.setStatus(LodaEntry.STATUS.CANCEL);
            DataChanger.getInstance().postStatus(entry);
        }
    }

    private void resumeLoda(LodaEntry entry) {
        addLoda(entry);
    }

    private void pauseLoda(LodaEntry entry) {
        LodaTask task = tasks.remove(entry.getId());

        if (task != null) {
            task.pause();
        } else {
            queue.remove(entry);
            entry.setStatus(LodaEntry.STATUS.PAUSE);
            DataChanger.getInstance().postStatus(entry);
        }
    }

    private void startLoda(LodaEntry entry) {

        LodaTask task = new LodaTask(entry, handler);
        executors.execute(task);
        tasks.put(entry.getId(), task);

    }
}
