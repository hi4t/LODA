package com.imba.library;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by zace on 2015/5/10.
 */
public abstract class DataWatcher implements Observer {
    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof LodaEntry) {
            updata((LodaEntry) data);
        }
    }

    public abstract void updata(LodaEntry data);
}
