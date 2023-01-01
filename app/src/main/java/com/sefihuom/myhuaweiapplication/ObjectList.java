package com.sefihuom.myhuaweiapplication;


import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.endPoint;
import static com.sefihuom.myhuaweiapplication.utilities.ObjectOperations.ItemObjectData;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.obs.services.ObsConfiguration;
import com.sefihuom.myhuaweiapplication.utilities.AlertUtilities;
import com.sefihuom.myhuaweiapplication.utilities.ObjectOperations;

public class ObjectList extends AppCompatActivity implements ObjectListAdapter.ClickListener
{

    RecyclerView recyclerView;
    ObjectListAdapter adapter;
    LinearLayoutManager layoutManager;

    String bucketName = null;
    TextView tv;
    AlertUtilities progress;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object_list);

        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);

        recyclerView = findViewById(R.id.bucketListRecycler);
        tv = findViewById(R.id.info);
        layoutManager = new LinearLayoutManager(this);

        bucketName = getIntent().getStringExtra("bucketName");

     //   setPreparingList();

    }


    @Override
    public void onDelete() {
        setPreparingList();
    }

    @Override
    public void onAppent(String e) {
        tv.setText(e);
        tv.setOnClickListener(null);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    public void setPreparingList() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                ObjectOperations.getImageList(bucketName, new ObjectOperations.OnWorkingListener(){
                    @Override
                    public void showProgress() {
                        progress = new AlertUtilities(ObjectList.this, "Preparing List...", false);
                        progress.show();
                    }

                    @Override
                    public void onFailure(String e) {
                        progress.dismiss();
                    }


                    @Override
                    public void onSuccess() {
                        initViews();
                        progress.dismiss();
                    }

                    @Override
                    public void onAppend(String e) {
                        tv.setText(e);
                        tv.setOnClickListener(null);
                        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
                    }
                });
            }
        }).start();

    }


    private void initViews() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        fetchRecords();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void fetchRecords() {
        adapter = new ObjectListAdapter(this, ItemObjectData );
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    @Override
    public void onResume() {
        super.onResume();
        setPreparingList();
    }


}




