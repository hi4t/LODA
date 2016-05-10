package com.imba.library;

import android.util.Log;

/**
 * Created by zace on 2016/5/10.
 */
public class Trace {

    public static final String TAG = "roupia.com";
    private static final boolean DEBUG = true;


    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }
}
