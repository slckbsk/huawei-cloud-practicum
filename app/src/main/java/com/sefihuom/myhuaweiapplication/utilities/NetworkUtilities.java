package com.sefihuom.myhuaweiapplication.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public  class NetworkUtilities {

    public static boolean isConnected(Context context) {


       if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    }
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }
                    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                } else {
                    return false;
                }
            } else {

                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                return info != null && info.isConnectedOrConnecting();
            }
        }
        return false;
    }



}



