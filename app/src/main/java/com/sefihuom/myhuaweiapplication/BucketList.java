package com.sefihuom.myhuaweiapplication;


import static com.sefihuom.myhuaweiapplication.utilities.BucketOperations.ItemObjectData;
import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.endPoint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.obs.services.ObsConfiguration;
import com.sefihuom.myhuaweiapplication.utilities.AlertUtilities;
import com.sefihuom.myhuaweiapplication.utilities.BucketOperations;

public class BucketList extends AppCompatActivity implements BucketListAdapter.ClickListener
{

    RecyclerView recyclerView;
    BucketListAdapter adapter;
    LinearLayoutManager layoutManager;
    Button create;
    String bucketName = null;
    TextView tv;
    AlertUtilities progress;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bucket_list);

        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);

        recyclerView = findViewById(R.id.bucketListRecycler);
        create = findViewById(R.id.cirCreateButton);
        tv = findViewById(R.id.tv);
        layoutManager = new LinearLayoutManager(this);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertUtilities builder = new AlertUtilities(BucketList.this, "Enter Bucket Name", "CREATE");
                builder.radio_e.setVisibility(View.GONE);
                builder.editText_e.setHint("Backet Name");
                builder.editText_e.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.okey_e.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        bucketName = builder.editText_e.getText().toString().trim();
                        progress = new AlertUtilities(BucketList.this, "Bucket " + bucketName + " Creating..", false);

                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                BucketOperations.createBucket(bucketName, new BucketOperations.OnWorkingListener(){
                                    @Override
                                    public void showProgress() {
                                        progress.show();
                                    }

                                    @Override
                                    public void onFailure(String e) {
                                        progress.dismiss();
                                    }

                                    @Override
                                    public void onSuccess() {
                                        setPreparingList();
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

                        builder.dismiss();

                    }
                });

                builder.cancel_e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v13) {
                        builder.dismiss();
                    }
                });
                builder.show();
            }

        });
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

                BucketOperations.getBucketsList(new BucketOperations.OnWorkingListener(){
                    @Override
                    public void showProgress() {
                        progress = new AlertUtilities(BucketList.this, "Preparing List...", false);
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
        adapter = new BucketListAdapter(this, ItemObjectData );
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




