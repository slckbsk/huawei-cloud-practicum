package com.sefihuom.myhuaweiapplication;


import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.endPoint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.obs.services.ObsConfiguration;
import com.sefihuom.myhuaweiapplication.utilities.AlertUtilities;
import com.sefihuom.myhuaweiapplication.utilities.ObjectOperations;

public class ImageTagging extends AppCompatActivity
{

    TextView tv;
    AlertUtilities progress;
    String bucketUrlform;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_tagging);

        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        tv = findViewById(R.id.tv);
        bucketUrlform = getIntent().getStringExtra("bucketUrlform");;

                RunImageTaggingSolution();

    }


    public void RunImageTaggingSolution () {
        AlertUtilities builder = new AlertUtilities(this, "Enter Image Url", "RUN");
        builder.radio_e.setVisibility(View.GONE);
        builder.editText_e.setText(bucketUrlform);
        builder.editText_e.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.okey_e.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String url = builder.editText_e.getText().toString().trim();
                progress = new AlertUtilities(ImageTagging.this, "Image Tagging Running...", true);

                     if (!url.equals("")){

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                     ObjectOperations.RunImageTaggingSolution(url, new ObjectOperations.OnWorkingListener(){
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

                } else {
                    tv.setText("Image url is necessary");
                    tv.setOnClickListener(null);
                    tv.setMovementMethod(ScrollingMovementMethod.getInstance());
                }

                builder.dismiss();

            }
        });

        builder.cancel_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v13) {
                builder.dismiss();
                onBackPressed();
            }
        });
        builder.show();

    }



}
