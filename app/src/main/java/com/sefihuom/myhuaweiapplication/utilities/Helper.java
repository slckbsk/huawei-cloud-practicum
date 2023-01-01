package com.sefihuom.myhuaweiapplication.utilities;

public class Helper {

    public static String getFileNameNoExtensions(String url) {
        String urlName = url.substring( url.lastIndexOf('/')+1, url.length() );
        return urlName.substring(0, urlName.lastIndexOf("."));
    }

    public static String getFileNameWithExtensions(String url) {
        return url.substring(url.lastIndexOf("/")+1);
    }

    public static String getFileExtensions(String file) {
        return file.substring(file.lastIndexOf("."));
    }

}
