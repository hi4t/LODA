package com.imba.loda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.imba.library.DataWatcher;
import com.imba.library.LodaEntry;
import com.imba.library.LodaManager;
import com.imba.library.Trace;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button download, pause, cancel;
    private LodaEntry entry;
    private LodaManager mlodaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mlodaManager = LodaManager.getInstance(this);
    }

    private DataWatcher watcher = new DataWatcher() {
        @Override
        public void updata(LodaEntry data) {
            entry = data;
            if (data.getStatus() == LodaEntry.STATUS.CANCEL) {
                entry = null;
            }
            Trace.i(data.toString());
        }
    };


    private void initView() {
        download = (Button) findViewById(R.id.download);
        download.setOnClickListener(this);
        pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        mlodaManager.addObserver(watcher);
        super.onResume();
    }


    @Override
    protected void onPause() {
        mlodaManager.removeObserver(watcher);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                entry = new LodaEntry();
                entry.setUrl("");
                entry.setId("1");
                mlodaManager.add(entry);

                break;
            case R.id.pause:
                if (entry.getStatus() == LodaEntry.STATUS.PAUSE) {
                    pause.setText("pause");
                    mlodaManager.resume(entry);
                    return;
                }

                mlodaManager.pause(entry);

                pause.setText("resume");
                break;
            case R.id.cancel:
                mlodaManager.cancel(entry);
                break;
        }
    }
}
