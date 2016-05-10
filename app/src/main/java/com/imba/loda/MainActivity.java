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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private DataWatcher watcher = new DataWatcher() {
        @Override
        public void updata(LodaEntry data) {
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
        LodaManager.getInstance().addObserver(watcher);
        super.onResume();
    }


    @Override
    protected void onPause() {
        LodaManager.getInstance().removeObserver(watcher);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                LodaEntry entry = new LodaEntry();
                entry.setUrl("");

                LodaManager.getInstance().add(this, entry);

                break;
            case R.id.pause:

                break;
            case R.id.cancel:

                break;
        }
    }
}
