package com.imba.library;

/**
 * Created by zace on 2015/5/10.
 */
public class LodaTask {

    private final LodaEntry entry;
    private boolean isPause;
    private boolean isCancel;

    public LodaTask(LodaEntry entry) {
        this.entry = entry;
    }

    public void start() {

        entry.setStatus(LodaEntry.STATUS.DOWNLOADING);
        DataChanger.getInstance().postStatus(entry);

        entry.totalLength = 1024 * 10;

        while (entry.currentLength < entry.totalLength) {

            if (isPause || isCancel) {
                entry.setStatus(isPause ? LodaEntry.STATUS.PAUSE : LodaEntry.STATUS.CANCEL);
                DataChanger.getInstance().postStatus(entry);
                return;
            }

            entry.currentLength += 1024;
            DataChanger.getInstance().postStatus(entry);
        }

        entry.setStatus(LodaEntry.STATUS.COMPLETE);
        DataChanger.getInstance().postStatus(entry);
    }

    public void pause() {
    }

    public void cancel() {
    }
}
