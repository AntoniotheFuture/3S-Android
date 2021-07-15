package com.nonemin.simplescreensaver.helper;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.Request;

import okhttp3.OkHttpClient;

public class okHttpRequest {
    private OkHttpClient HttpClient;


    public okHttpRequest(OkHttpClient HttpClient){
        this.HttpClient = HttpClient;
    }

    public String Request(String url,HashMap<String,String> parameters) throws Exception{
        final Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder;
        Request request;
        urlBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        for (String parameter:parameters.keySet()) {
            String val = parameters.get(parameter);
            urlBuilder.addQueryParameter(parameter,val);
        }
        HttpUrl httpUrl = urlBuilder.build();
        reqBuild.url(httpUrl);
        request = reqBuild.build();
        try {
            okhttp3.Response response = HttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseString = (response.body() == null ? "" : Objects.requireNonNull(response.body()).string());
                response.close();
                return responseString;
            } else {
                response.close();
                throw new Exception();
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
