package com.sefihuom.myhuaweiapplication.utilities;

import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.ak;
import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.bucketLocation;
import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.endPoint;
import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.sk;

import android.annotation.SuppressLint;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.AccessControlList;
import com.obs.services.model.BucketMetadataInfoRequest;
import com.obs.services.model.BucketMetadataInfoResult;
import com.obs.services.model.BucketStorageInfo;
import com.obs.services.model.ListBucketsRequest;
import com.obs.services.model.ObsBucket;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.StorageClassEnum;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BucketOperations {

    public static final UiThread uiThread = new UiThread();
    public static List<ObsBucket> ItemObjectData;


    public static void deleteBucket(String bucketName,
                                    OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {
            sb.append("Deleting bucket ").append(bucketName).append("\n");
            obsClient.deleteBucket(bucketName);
            sb.append("Deleting bucket: ").append(bucketName).append(" successfully!\n");
            postAppent(listener, sb.toString());
            postSuccess(listener);

        } catch (ObsException e) {
            sb.append("\n");
            sb.append("Response Code:").append(e.getResponseCode())
                    .append("\n").append("Error Message:").append(e.getErrorMessage())
                    .append("\n").append("Error Code:").append(e.getErrorCode())
                    .append("\n").append("Request ID:").append(e.getErrorRequestId())
                    .append("\n").append("Host ID:").append(e.getErrorHostId());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (Exception e) {
            sb.append("\n");
            sb.append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                postAppent(listener, e.toString());
                postFailure(listener, e.getMessage());
            }
        }
    }

    public static void createBucket(String bucketName,
                                    OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {
            sb.append("Create bucket ").append(bucketName).append("\n");
            ObsBucket obsBucket = new ObsBucket();
            obsBucket.setBucketName(bucketName);
            obsBucket.setLocation(bucketLocation);
            obsBucket.setBucketStorageClass(StorageClassEnum.STANDARD);
            obsClient.createBucket(obsBucket);

            sb.append("\n");
            sb.append("Create bucket:").append(bucketName).append(" successfully!\n");
            postAppent(listener, sb.toString());
            postSuccess(listener);


        } catch (ObsException e) {

            sb.append("\n");
            sb.append("Response Code:").append(e.getResponseCode())
                    .append("\n").append("Error Message:").append(e.getErrorMessage())
                    .append("\n").append("Error Code:").append(e.getErrorCode())
                    .append("\n").append("Request ID:").append(e.getErrorRequestId())
                    .append("\n").append("Host ID:").append(e.getErrorHostId());

            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (Exception e) {
            sb.append("\n");
            sb.append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                postAppent(listener, e.toString());
                postFailure(listener, e.getMessage());
            }
        }

    }


    public static void addBucketPolicy(String bucketName,
                                       OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {
            String myPolicy =
                    "{\"Statement\":[{\"Principal\":\"*\",\"Effect\":\"Allow\",\"Action\":\"ListBucket\",\"Resource\":\""+bucketName+"\"}]}";
            obsClient.setBucketPolicy(bucketName, myPolicy);
            sb.append("\n");
            sb.append("Add policy:").append(bucketName).append(" successfully!\n");
            postAppent(listener, sb.toString());
            postSuccess(listener);


        } catch (ObsException e) {

            sb.append("\n");
            sb.append("Response Code:").append(e.getResponseCode())
                    .append("\n").append("Error Message:").append(e.getErrorMessage())
                    .append("\n").append("Error Code:").append(e.getErrorCode())
                    .append("\n").append("Request ID:").append(e.getErrorRequestId())
                    .append("\n").append("Host ID:").append(e.getErrorHostId());

            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (Exception e) {
            sb.append("\n");
            sb.append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                postAppent(listener, e.toString());
                postFailure(listener, e.getMessage());
            }
        }

    }


    public static void getBucketsList(OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ItemObjectData = new ArrayList<>();
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {

            ListBucketsRequest request = new ListBucketsRequest();
            request.setQueryLocation(true);
            List<ObsBucket> buckets = obsClient.listBuckets(request);

            if (buckets.size() != 0) {
                for (ObsBucket bucket : buckets) {

                    ObsBucket obsBucket = new ObsBucket(
                            bucket.getBucketName(),
                            bucket.getLocation());
                    ItemObjectData.add(obsBucket);

                    postAppent(listener, sb.toString());
                    postSuccess(listener);
                }
            } else {
                sb.append("\n");
                sb.append("No Buckets");
                postAppent(listener, sb.toString());
                postSuccess(listener);
            }


        } catch (ObsException e) {
            sb.append("\n");
            sb.append("Response Code:").append(e.getResponseCode())
                    .append("\n").append("Error Message:").append(e.getErrorMessage())
                    .append("\n").append("Error Code:").append(e.getErrorCode())
                    .append("\n").append("Request ID:").append(e.getErrorRequestId())
                    .append("\n").append("Host ID:").append(e.getErrorHostId());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (Exception e) {
            sb.append("\n");
            sb.append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                postAppent(listener, e.toString());
                postFailure(listener, e.getMessage());
            }
        }
    }


    public static void obtainingBucketMetadata(String bucketName, OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {

            BucketMetadataInfoRequest request = new BucketMetadataInfoRequest(bucketName);
            request.setOrigin("http://www.a.com");

            BucketMetadataInfoResult result = obsClient.getBucketMetadata(request);

            sb.append("\n");
            sb.append("BUCKET:").append(bucketName)
                    .append("\n").append("Bucket Storage Class:").append(result.getBucketStorageClass())
                    .append("\n").append("Allow Origin:").append(result.getAllowOrigin())
                    .append("\n").append("Max Age:").append(result.getMaxAge())
                    .append("\n").append("Allow Headers:").append(result.getAllowHeaders())
                    .append("\n").append("Allow Methods:").append(result.getAllowMethods())
                    .append("\n").append("Expose Headers:").append(result.getExposeHeaders())
                    .append("\n").append("-------------------------");

            postAppent(listener, sb.toString());
            postSuccess(listener);


        } catch (ObsException e) {
            sb.append("\n");
            sb.append("Response Code:").append(e.getResponseCode())
                    .append("\n").append("Error Message:").append(e.getErrorMessage())
                    .append("\n").append("Error Code:").append(e.getErrorCode())
                    .append("\n").append("Request ID:").append(e.getErrorRequestId())
                    .append("\n").append("Host ID:").append(e.getErrorHostId());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (Exception e) {
            sb.append("\n");
            sb.append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());
        }
    }


    @SuppressLint("LongLogTag")
    public static void bucketStorageInfo(String bucketName, OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {

            BucketStorageInfo storageInfo = obsClient.getBucketStorageInfo(bucketName);

            sb.append("\n");
            sb.append("BUCKET:").append(bucketName)
                    .append("\n").append("Bucket Storage Class:").append(storageInfo.getObjectNumber())
                    .append("\n").append("Allow Origin:").append(storageInfo.getSize())
                    .append("\n").append("-------------------------");

            postAppent(listener, sb.toString());
            postSuccess(listener);


        } catch (ObsException e) {
            sb.append("\n");
            sb.append("Response Code:").append(e.getResponseCode())
                    .append("\n").append("Error Message:").append(e.getErrorMessage())
                    .append("\n").append("Error Code:").append(e.getErrorCode())
                    .append("\n").append("Request ID:").append(e.getErrorRequestId())
                    .append("\n").append("Host ID:").append(e.getErrorHostId());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (Exception e) {
            sb.append("\n");
            sb.append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                postAppent(listener, e.toString());
                postFailure(listener, e.getMessage());
            }
        }
    }


    public static void createFolder(String bucketName, String folderName, OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {

            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(folderName + "/");
            obsClient.putObject(request);

            sb.append("Creating an empty folder ").append(folderName).append("\n");
            postAppent(listener, sb.toString());
            postSuccess(listener);


        } catch (ObsException e) {
            sb.append("\n");
            sb.append("Response Code:").append(e.getResponseCode())
                    .append("\n").append("Error Message:").append(e.getErrorMessage())
                    .append("\n").append("Error Code:").append(e.getErrorCode())
                    .append("\n").append("Request ID:").append(e.getErrorRequestId())
                    .append("\n").append("Host ID:").append(e.getErrorHostId());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (Exception e) {
            sb.append("\n");
            sb.append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());
        }
    }

    public static void upLoadFiles(int number, String bucketName, String objectName, String url, OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);


        try {

            if (number == 1) {
                obsClient.putObject(bucketName, objectName, new File(url));

                PutObjectRequest request = new PutObjectRequest();
                request.setBucketName(bucketName);
                request.setObjectKey(objectName);
                request.setFile(new File(url));
                obsClient.putObject(request);
                obsClient.close();

                sb.append("Upload object: ").append(objectName).append(" successfully!\n");
                postAppent(listener, sb.toString());
                postSuccess(listener);
            }

            if (number == 2) {
                InputStream inputStream = new URL(url).openStream();
                obsClient.putObject(bucketName, objectName, inputStream);

                /*
                PutObjectRequest request = new PutObjectRequest();
                request.setBucketName(bucketName);
                request.setObjectKey(objectName);
                obsClient.putObject(request);

                 */

                obsClient.close();


                sb.append("Upload object: ").append(objectName).append(" successfully!\n");
                postAppent(listener, sb.toString());
                postSuccess(listener);
            }

        } catch (ObsException e) {

            sb.append("Response Code:").append(e.getResponseCode())
                    .append("\n").append("Error Message:").append(e.getErrorMessage())
                    .append("\n").append("Error Code:").append(e.getErrorCode())
                    .append("\n").append("Request ID:").append(e.getErrorRequestId())
                    .append("\n").append("Host ID:").append(e.getErrorHostId());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (Exception e) {

            sb.append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                postAppent(listener, e.toString());
                postFailure(listener, e.getMessage());
            }
        }
    }


    public interface OnWorkingListener {
        void showProgress();

        void onFailure(String e);

        void onSuccess();

        void onAppend(String e);
    }


    private static void postSuccess(OnWorkingListener listener) {
        uiThread.execute(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess();
            }
        });
    }

    private static void postFailure(OnWorkingListener listener, final String e) {
        uiThread.execute(new Runnable() {
            @Override
            public void run() {
                listener.onFailure(e);
            }
        });
    }

    private static void postProgress(OnWorkingListener listener) {
        uiThread.execute(new Runnable() {
            @Override
            public void run() {
                listener.showProgress();
            }
        });
    }

    private static void postAppent(OnWorkingListener listener, final String e) {
        uiThread.execute(new Runnable() {
            @Override
            public void run() {
                listener.onAppend(e);
            }
        });
    }


}