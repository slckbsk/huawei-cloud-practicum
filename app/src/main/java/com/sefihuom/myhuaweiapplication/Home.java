package com.sefihuom.myhuaweiapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.obs.services.ObsClient;
import com.obs.services.model.ProtocolEnum;
import com.obs.services.model.RedirectAllRequest;
import com.obs.services.model.WebsiteConfiguration;

public class Home extends AppCompatActivity
{

    EditText akT, skT, endPointT, bucketlocationT;
    public static String endPoint, ak ,sk ,bucketlocation;
    Button list;
    TextView huaweiAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        akT = findViewById(R.id.editTextAK);
        skT = findViewById(R.id.editTextSK);
        endPointT = findViewById(R.id.editTextendPoint);
        bucketlocationT = findViewById(R.id.editTextLocation);
        list = findViewById(R.id.cirLoginButton);
        huaweiAccount = findViewById(R.id.accountInfo);

        endPoint = endPointT.getText().toString();
        ak = akT.getText().toString();
        sk = skT.getText().toString();
        bucketlocation = bucketlocationT.getText().toString();






        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                endPoint = endPointT.getText().toString();
                ak = akT.getText().toString();
                sk = skT.getText().toString();
                bucketlocation = bucketlocationT.getText().toString();

                if ( !endPoint.matches("") && !ak.matches("") && !sk.matches("")  && !bucketlocation.matches("") ) {

                    Intent intent = new Intent(Home.this, BucketList.class);
                    startActivity(intent);

                } else {

                  //  Toast.makeText(getApplicationContext(),"All fields are required and cannot be empty", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Home.this, BucketList.class);
                    startActivity(intent);

                }

            }
        });

        huaweiAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "https://www.huaweicloud.com/intl/en-us/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }


}

