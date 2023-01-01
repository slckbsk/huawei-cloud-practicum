package com.sefihuom.myhuaweiapplication;

import static com.sefihuom.myhuaweiapplication.utilities.Helper.getFileNameWithExtensions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sefihuom.myhuaweiapplication.utilities.AlertUtilities;
import com.sefihuom.myhuaweiapplication.utilities.BucketOperations;
import com.sefihuom.myhuaweiapplication.utilities.FileUtils;

import java.io.File;

public class Bucket extends AppCompatActivity
{
    AlertUtilities progress;
    String bucket;
    TextView tv,info;
    Button createFolder, upload, objectList, addPolicy;
    int myNumber;
    String title;
    String file;
    AlertUtilities builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bucket);


        bucket = getIntent().getStringExtra("bucketName");

        tv = findViewById(R.id.tv);
        info = findViewById(R.id.info);
        createFolder = findViewById(R.id.cirCreateFolder);
        upload = findViewById(R.id.cirUpLoad);
        objectList = findViewById(R.id.cirObjectList);
        addPolicy = findViewById(R.id.cirPolicy);

         file=null;
        builder = new AlertUtilities(this, "Enter File location", "UPLOAD");
     //   setPreparingList();

        addPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPolicy();
            }
        });




        createFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFolder();
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpload();
            }
        });


        objectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bucket.this, ObjectList.class);
                intent.putExtra("bucketName", bucket);
                startActivity(intent);
            }
        });
    }



    public void setPreparingList() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                BucketOperations.bucketStorageInfo(bucket, new BucketOperations.OnWorkingListener(){
                    @Override
                    public void showProgress() {
                        progress = new AlertUtilities(Bucket.this, "Prepering Bucket " + bucket + "...", false);
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
                        info.setText(e);
                        info.setOnClickListener(null);
                        info.setMovementMethod(ScrollingMovementMethod.getInstance());
                    }
                });
            }
        }).start();

    }


    public void addPolicy() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                BucketOperations.addBucketPolicy(bucket, new BucketOperations.OnWorkingListener(){
                    @Override
                    public void showProgress() {
                        progress = new AlertUtilities(Bucket.this, "Adding Policy " + bucket + "...", false);
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

    public void createFolder() {

        AlertUtilities builder = new AlertUtilities(this, "Enter Folder Name", "CREATE");
        builder.radio_e.setVisibility(View.GONE);
        builder.editText_e.setHint("Folder Name");
        builder.editText_e.setInputType(InputType.TYPE_CLASS_TEXT);


        builder.okey_e.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String folderName = builder.editText_e.getText().toString().trim();
                progress = new AlertUtilities(Bucket.this, folderName + " Creating Folder...", true);

                if (!folderName.equals("")){

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        BucketOperations.createFolder(bucket, folderName, new BucketOperations.OnWorkingListener(){
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
                                setPreparingList();
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
                    tv.setText(R.string.folder_name_is_necessary);
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
            }
        });
        builder.show();

    }


    public void setUpload () {

        builder.radio_e.setVisibility(View.VISIBLE);
        builder.editText_e.setVisibility(View.GONE);
        builder.editText_e.setHint("File location");
        builder.editText_e.setInputType(InputType.TYPE_CLASS_TEXT);


        builder.setOnNumberChangeListener(new AlertUtilities.ChangeListener() {

            @Override
            public void onChange(int e) {

                myNumber = e;
                builder.editText_e.setVisibility(View.VISIBLE);
                builder.radio_e.setVisibility(View.GONE);

                if (myNumber == 1 ){

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkStoragePermission()) {
                            pickFile();

                        }
                    } else {
                        pickFile();
                    }

                }

            }
        });



        builder.okey_e.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                file = builder.editText_e.getText().toString();
                title = getFileNameWithExtensions(file);
                progress = new AlertUtilities(Bucket.this, title + " Uploding File...", true);


                if (!file.equals("")){

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            BucketOperations.upLoadFiles(myNumber, bucket, title, file ,new BucketOperations.OnWorkingListener(){
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

                } else {
                    tv.setText(R.string.file_location_empty);
                    tv.setOnClickListener(null);
                    tv.setMovementMethod(ScrollingMovementMethod.getInstance());
                }

                builder.dismiss();
                builder.editText_e.setText("");
                builder.editText_e.setHint("File location");

            }
        });

        builder.cancel_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v13) {
                builder.dismiss();
                builder.editText_e.setText("");
                builder.editText_e.setHint("File location");
            }
        });
        builder.show();

    }


    private void pickFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        String[] mimetypes = { "image/*" };
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, 1);
    }



    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                if (data != null) {

                    Uri uri = data.getData();

                    try {

                        File originalFile = new File(FileUtils.getRealPath(this, uri));
                        file = originalFile.getPath();
                        title = originalFile.getName();
                        builder.editText_e.setText(file);

                    } catch (Exception e) {
                        Log.e("getTag()", "File select error", e);
                        Toast.makeText(this, "LINK CANT PLAY", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(this, "SORRY", Toast.LENGTH_LONG).show();
                }
            }
        }

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
            pickFile();

        } else if (requestCode == 1 && grantResults.length > 0
                && (grantResults[0] == PackageManager.PERMISSION_DENIED
                || grantResults[1] == PackageManager.PERMISSION_DENIED)) {
            needStoragePermission();
            Toast.makeText(this, "WRITE EXTERNAL STORAGE PERMISSION DENIED", Toast.LENGTH_SHORT).show();
        }
    }

@Override
public  void  onResume() {
    super.onResume();
    setPreparingList();
    tv.setText("");
}



}
