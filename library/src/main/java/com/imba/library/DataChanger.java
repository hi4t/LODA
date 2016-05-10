package com.imba.library;

import java.util.Observable;

/**
 * Created by zace on 2015/5/10.
 */
public class DataChanger extends Observable {

    private static DataChanger dataChanger;

    private DataChanger() {
    }

    protected static synchronized DataChanger getInstance() {
        if (dataChanger == null) {
            dataChanger = new DataChanger();
        }
        return dataChanger;
    }


    public void postStatus(LodaEntry entry) {
        setChanged();
        notifyObservers(entry);
    }

}
