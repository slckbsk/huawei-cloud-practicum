package com.sefihuom.myhuaweiapplication.utilities;


import static com.sefihuom.myhuaweiapplication.Object.DIRECTORY;
import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.ak;
import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.endPoint;
import static com.sefihuom.myhuaweiapplication.utilities.HuaweiAccount.sk;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.image.v2.ImageClient;
import com.huaweicloud.sdk.image.v2.model.ImageTaggingReq;
import com.huaweicloud.sdk.image.v2.model.RunImageTaggingRequest;
import com.huaweicloud.sdk.image.v2.model.RunImageTaggingResponse;
import com.huaweicloud.sdk.image.v2.region.ImageRegion;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.DownloadFileRequest;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObsObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectOperations {

    public static final UiThread uiThread = new UiThread();
    public static List<ObsObject> ItemObjectData;


    public static void RunImageTaggingSolution (String imgUrl, OnWorkingListener listener) {

        postProgress(listener);
        StringBuilder sb = new StringBuilder();
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        try {

            ImageClient client = ImageClient.newBuilder()
                    .withCredential(auth)
                    .withRegion(ImageRegion.valueOf("ap-southeast-1"))
                    .build();

            RunImageTaggingRequest request = new RunImageTaggingRequest();
            ImageTaggingReq body = new ImageTaggingReq();
            body.withLimit(50);
            body.withThreshold(95f);
            body.withLanguage("en");
            body.withUrl(imgUrl);
            request.withBody(body);

            RunImageTaggingResponse response = client.runImageTagging(request);
            sb.append("\n");
            sb.append(response.toString());

            postAppent(listener, sb.toString());
            postSuccess(listener);

        } catch (ConnectionException | RequestTimeoutException e) {
            e.printStackTrace();
            sb.append("\n");
            sb.append("Http Status Code:").append(e.getMessage());
            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        } catch (ServiceResponseException e) {
            e.printStackTrace();
            sb.append("\n");
            sb.append("Http Status Code:").append(e.getHttpStatusCode())
                    .append("\n").append("Error Message:").append(e.getErrorMsg())
                    .append("\n").append("Error Code:").append(e.getErrorCode());

            postAppent(listener, sb.toString());
            postFailure(listener, e.getMessage());

        }
    }


    public static void getImageList (String bucketName, OnWorkingListener listener){
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ItemObjectData = new ArrayList<>();
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try  {

            ObjectListing result = obsClient.listObjects(bucketName);
            List<ObsObject> objectList = result.getObjects();

            if (objectList.size() != 0) {

                for(ObsObject obsObject : objectList){

                    ItemObjectData.add(obsObject);
                    postAppent(listener, sb.toString());
                    postSuccess(listener);

                }

            } else {

                sb.append("\n");
                sb.append("No Object");
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
        }
    }





    public static void deleteObject (String bucketName, String objectName, OnWorkingListener listener) {

        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ItemObjectData = new ArrayList<>();
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {

            obsClient.deleteObject(bucketName, objectName);
            sb.append("Deleted object ").append(objectName).append(" successfully!\n");
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


    public static void addObjectPolicy  (String objectName,
                                       OnWorkingListener listener) {
        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {
            String myPolicy =
                    "{\"Statement\":[{\"Principal\":\"*\",\"Effect\":\"Allow\",\"Action\":\"ListBucket\",\"Resource\":\""+objectName+"\"}]}";
            obsClient.setBucketPolicy(objectName, myPolicy);
            sb.append("\n");
            sb.append("Add policy:").append(objectName).append(" successfully!\n");
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


    public static void downloadObject (String bucketName, String objectName, OnWorkingListener listener) {

        StringBuilder sb = new StringBuilder();
        postProgress(listener);
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        DownloadFileRequest request = new DownloadFileRequest(bucketName, objectName);
        request.setDownloadFile((DIRECTORY + "/" + objectName));
        request.setTaskNum(5);
        request.setPartSize(10 * 1024 * 1024);
        request.setEnableCheckpoint(true);


        try {

            obsClient.downloadFile(request);

            sb.append("\n");
            sb.append("Downloaded ").append(objectName).append(" successfully!\n");
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