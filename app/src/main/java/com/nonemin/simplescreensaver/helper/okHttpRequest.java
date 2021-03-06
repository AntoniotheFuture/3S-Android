/*
MIT License

Copyright (c) 2021 梁小蜗

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
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
