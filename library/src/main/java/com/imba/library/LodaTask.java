package com.imba.library;

import android.os.Handler;
import android.os.Message;

/**
 * Created by zace on 2015/5/10.
 */
public class LodaTask implements Runnable {

    private final LodaEntry entry;
    private final Handler handler;
    private volatile boolean isPause;
    private volatile boolean isCancel;

    public LodaTask(LodaEntry entry, Handler handler) {
        this.entry = entry;
        this.handler = handler;
    }

    public void start() {

        entry.setStatus(LodaEntry.STATUS.DOWNLOADING);
        postStatus(entry);

        entry.totalLength = 1024 * 20;

        while (entry.currentLength < entry.totalLength) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isPause || isCancel) {
                entry.setStatus(isPause ? LodaEntry.STATUS.PAUSE : LodaEntry.STATUS.CANCEL);
                postStatus(entry);
                return;
            }

            entry.currentLength += 1024;
            postStatus(entry);
        }

        entry.setStatus(LodaEntry.STATUS.COMPLETE);
        postStatus(entry);
    }

    public void pause() {
        isPause = true;
    }

    public void cancel() {
        isCancel = true;
    }

    @Override
    public void run() {
        start();
    }

    public void postStatus(LodaEntry entry) {
        Message msg = handler.obtainMessage();
        msg.obj = entry;
        handler.sendMessage(msg);
    }


}
