package com.sefihuom.myhuaweiapplication;

import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.endPoint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sefihuom.myhuaweiapplication.utilities.AlertUtilities;
import com.sefihuom.myhuaweiapplication.utilities.BucketOperations;
import com.sefihuom.myhuaweiapplication.utilities.ObjectOperations;
import com.squareup.picasso.Picasso;

import java.io.File;

public class Object extends AppCompatActivity
{
    AlertUtilities progress;
    String bucketName, objectName, bucketUrlform, webUrlForm;
    TextView tv;
    ImageView imageView;
    Button delete, imageTagging, download, browserView, addPolicy;
    public static File DIRECTORY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.object);

        bucketName = getIntent().getStringExtra("bucketName");
        objectName = getIntent().getStringExtra("objectName");

        tv = findViewById(R.id.tv);
        imageView = findViewById(R.id.imageView);
        download = findViewById(R.id.cirDownload);
        imageTagging = findViewById(R.id.cirImageTagging);
        delete = findViewById(R.id.cirDelete);
        browserView = findViewById(R.id.cirBrowserView);
        addPolicy = findViewById(R.id.cirPolicy);

        bucketUrlform = "https://"+bucketName +"."+ endPoint+"/"+objectName;
        webUrlForm = "https://www.sefihuom.ml/"+objectName;

        tv.setText(bucketUrlform);
        Picasso.with(this).load(bucketUrlform).into(imageView);

        String folder_main = "Huawei-Image";
        DIRECTORY = new File(Environment.getExternalStorageDirectory(), folder_main);

        addPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPolicy();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ResultOfMethodCallIgnored")
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if (!DIRECTORY.exists()) {

                    if (Build.VERSION.SDK_INT >= 23) {

                        if (checkStoragePermission()) {
                            DIRECTORY.mkdir();
                            tv.setText(DIRECTORY + " READY");
                            tv.setOnClickListener(null);
                            tv.setMovementMethod(ScrollingMovementMethod.getInstance());
                        }
                    } else {
                        DIRECTORY.mkdir();
                        tv.setText(DIRECTORY + "READY");
                        tv.setOnClickListener(null);
                        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
                    }

                }
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkStoragePermission()) {
                        downloadImage();
                    }
                } else {
                    downloadImage();

                }



            }
        });

        imageTagging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Object.this, ImageTagging.class);
                intent.putExtra("bucketUrlform", bucketUrlform);
                startActivity(intent);
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });


        browserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = null;
                if (bucketName.equals("queenbucket")){
                    browserIntent  = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrlForm));
                } else {
                    browserIntent  = new Intent(Intent.ACTION_VIEW, Uri.parse(bucketUrlform));
                }

                startActivity(browserIntent);
            }
        });

    }


    public void addPolicy() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                ObjectOperations.addObjectPolicy(objectName, new ObjectOperations.OnWorkingListener(){
                    @Override
                    public void showProgress() {
                        progress = new AlertUtilities(Object.this, "Adding Policy " + objectName + "...", false);
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

    }


    public void deleteImage () {

        progress = new AlertUtilities(this, "Being Deleted...", true);

        new Thread(new Runnable() {

            @Override
            public void run() {

                ObjectOperations.deleteObject(bucketName, objectName, new ObjectOperations.OnWorkingListener(){
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
                        onBackPressed();

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


    public void downloadImage () {

        progress = new AlertUtilities(this, "Downloding...", true);

        new Thread(new Runnable() {

            @Override
            public void run() {

                ObjectOperations.downloadObject(bucketName, objectName, new ObjectOperations.OnWorkingListener(){
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
    }



    public boolean checkStoragePermission() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                return false;
            }
        }

        return true;
    }


    private void needStoragePermission() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("WARNING");
                builder.setMessage(" APP DOESN'T WORK WITHOUT PERMISSION");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        requestPermissions(
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                    }
                });

                builder.show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "READ EXTERNAL STORAGE PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            downloadImage();

        } else if (requestCode == 1 && grantResults.length > 0
                && (grantResults[0] == PackageManager.PERMISSION_DENIED
                || grantResults[1] == PackageManager.PERMISSION_DENIED)) {
            needStoragePermission();
            Toast.makeText(this, "WRITE EXTERNAL STORAGE PERMISSION DENIED", Toast.LENGTH_SHORT).show();
        }
    }

}






