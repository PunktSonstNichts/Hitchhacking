package com.landtanin.hitchhacker;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by landtanin on 11/19/2016 AD.
 */

public class JSONObtained {
    private static final String BASE_URL = "http://10.232.29.59/HitchHacking/Hitchhacking/server/";
    private static OkHttpClient client = null;

    public static OkHttpClient getInstance() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)// connect timeout
                    .writeTimeout(200, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS) // socket timeout
                    .build();

        }

        return client;
    }
    public static Request getRequest(HttpUrl url){

        Request request = new Request.Builder()
                .url(url)
                .build();
        return request;
    }

    public static Request postRequest(HttpUrl url, RequestBody formBody){

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        return request;
    }

    public static String getAbsoluteUrl(String relativeUrl) {

        return BASE_URL + relativeUrl;
    }

}