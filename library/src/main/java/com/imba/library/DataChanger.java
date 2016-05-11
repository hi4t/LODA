package com.imba.library;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by zace on 2015/5/10.
 */
public class DataChanger extends Observable {

    private static DataChanger dataChanger;
    private LinkedHashMap<String, LodaEntry> entrys;
    private ArrayList<LodaEntry> entryList;

    private DataChanger() {
        entrys = new LinkedHashMap<>();
        entryList = new ArrayList<>();
    }

    protected static synchronized DataChanger getInstance() {
        if (dataChanger == null) {
            dataChanger = new DataChanger();
        }
        return dataChanger;
    }


    public void postStatus(LodaEntry entry) {
        setChanged();
        entrys.put(entry.getId(), entry);
        notifyObservers(entry);
    }

    public ArrayList<LodaEntry> getAllPauseEntry() {

        if (entrys != null) {

            for (Map.Entry<String, LodaEntry> entryMap : entrys.entrySet()) {
                entryList.add(entryMap.getValue());
            }

            return entryList;
        }

        return null;
    }


}
