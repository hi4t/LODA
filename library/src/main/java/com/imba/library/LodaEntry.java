package com.imba.library;

import java.io.Serializable;

/**
 * Created by zace on 2015/5/10.
 */
public class LodaEntry implements Serializable {

    private String id;
    private String url;
    public long currentLength;
    public long totalLength;
    private STATUS status;

    public enum STATUS {DOWNLOADING, COMPLETE, CANCEL, PAUSE}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public STATUS getStatus() {
        return status;
    }

    protected void setStatus(STATUS status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "entry status is " + getStatus() + " and the download is " + currentLength + " / " + totalLength;
    }
}
