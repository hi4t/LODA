package com.imba.loda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imba.library.DataWatcher;
import com.imba.library.LodaEntry;
import com.imba.library.LodaManager;
import com.imba.library.Trace;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private LodaManager mlodaManager;
    private RecyclerView mRecyclerView;
    private ListAdapter adapter;
    private ArrayList<LodaEntry> entrys = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        initData();
        initView();
        mlodaManager = LodaManager.getInstance(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            entrys.add(new LodaEntry(i + "", "", LodaEntry.STATUS.IDLE));
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);


        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置adapter
        mRecyclerView.setAdapter(adapter = new ListAdapter());
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//      //添加分割线
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(
//                getActivity(), DividerItemDecoration.HORIZONTAL_LIST));

    }

    private DataWatcher watcher = new DataWatcher() {
        @Override
        public void updata(LodaEntry data) {
            int index = entrys.indexOf(data);
            if (index != -1) {
                entrys.remove(index);
                entrys.add(index, data);
                adapter.notifyDataSetChanged();
            }
        }
    };


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

    /**
     * toolbar按钮点击回掉
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String title = item.getTitle().toString();
        if ("全部开始".equals(title)) {
            mlodaManager.recoverAll();
        } else {
            mlodaManager.pauseAll();
        }
        item.setTitle(title.equals("全部开始") ? "全部暂停" : "全部开始");

        return true;
    }


    class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.list_item, parent,
                    false));

            return holder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.tv.setText(entrys.get(position).toString());


            final LodaEntry entry = entrys.get(position);
            holder.bt.setText(entrys.get(position).getStatus().toString());
            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (entry.getStatus()) {
                        case WAITING:
                            break;
                        case IDLE:
                            mlodaManager.add(entry);
                            break;
                        case PAUSE:
                            mlodaManager.resume(entry);
                            break;
                        case DOWNLOADING:
                            mlodaManager.pause(entry);
                            break;

                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return entrys.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            Button bt;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.progress);
                bt = (Button) view.findViewById(R.id.down);
            }
        }
    }

}
