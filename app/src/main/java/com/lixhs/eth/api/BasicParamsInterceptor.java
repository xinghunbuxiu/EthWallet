package com.lixhs.eth.api;

import android.text.TextUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


/**
 * Created by jk.yeo on 16/3/4 15:28.
 * Mail to ykooze@gmail.com
 */
public class BasicParamsInterceptor implements Interceptor {
    Map<String, String> queryParamsMap = new HashMap<>();
    Map<String, String> paramsMap = new HashMap<>();
    Map<String, String> headerParamsMap = new HashMap<>();
    List<String> headerLinesList = new ArrayList<>();

    private BasicParamsInterceptor() {

    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        try {
//            requestBuilder.header("User-Agent", "ANDROID");
            requestBuilder.header("User-Agent", "IChunQiuExoPlayer/3.5.0.52 (Linux;Android 5.1.1) ExoPlayerLib/2.3.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.addHeader(request, requestBuilder);
        // process header params inject
        Headers.Builder headerBuilder = request.headers().newBuilder();
        if (this.headerParamsMap.size() > 0) {
            Iterator iterator = this.headerParamsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }

        if (this.headerLinesList.size() > 0) {
            for (String line : this.headerLinesList) {
                headerBuilder.add(line);
            }
            requestBuilder.headers(headerBuilder.build());
        }
        // process header params end


        // process queryParams inject whatever it's GET or POST
        if (this.queryParamsMap.size() > 0) {
            if (request.method().equals("GET")) {
                request = this.injectParamsIntoUrl(request.url().newBuilder(), requestBuilder, this.queryParamsMap);
            }
        }
        paramsMap.clear();
        // process post body inject
        if (this.paramsMap.size() > 0) {
            if (this.canInjectIntoBody(request)) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Entry<String, String> entry : this.paramsMap.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue());
                }

                FormBody formBody = formBodyBuilder.build();
                String postBodyString = BasicParamsInterceptor.bodyToString(request.body());
//                postBodyString += (postBodyString.length() > 0 ? "&" : "") + BasicParamsInterceptor.bodyToString(formBody);
                postBodyString = bodyToString(formBody);
                requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
            }
        }

        request = requestBuilder.build();
        return chain.proceed(request);
    }

    static String pathSegmentsToString(List<String> pathSegments) {
        StringBuilder out = new StringBuilder();
        for (int i = 0, size = pathSegments.size(); i < size; i++) {
            out.append('/');
            out.append(pathSegments.get(i));
        }
        return out.toString();
    }

    private void addHeader(Request request, Request.Builder build) {
        String method = request.method();
        Map<String, String> headMap = new HashMap<>();
        if ("POST".equals(method)) {
            //build.put(RequestBody.create());
            Map<String, String> map = new HashMap<>();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    map.put(body.encodedName(i), URLDecoder.decode(body.encodedValue(i)));
                }
                for (Entry<String, String> entry : this.paramsMap.entrySet()) {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
//            headMap = CommonUnit.getUserToken(map, pathSegments);
        } else if ("GET".equals(method)) {
            if (this.queryParamsMap.size() > 0) {
                request = this.injectParamsIntoUrl(request.url().newBuilder(), build, this.queryParamsMap);
            }
            String str = request.url().query();
            if (!TextUtils.isEmpty(str)) {
                if (str.contains("&")) {
                    for (String key : str.split("&")) {
                        headMap.put(key.split("=")[0], key.split("=")[1]);
                    }
                } else if (str.contains("=")) {
                    headMap.put(str.split("=")[0], str.split("=")[1]);
                }
            }
//            headMap = CommonUnit.getUserToken(headMap,pathSegments);
        }
        build.addHeader("Content-Type", "application/json");
        for (String key : headMap.keySet()) {
            build.addHeader(key, headMap.get(key));
        }
    }

    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        return TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded");
    }

    // func to inject params into url
    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }

        return null;
    }

    private static String bodyToString(RequestBody request) {
        try {
            RequestBody copy = request;
            Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (IOException e) {
            return "did not work";
        }
    }

    public static class Builder {

        BasicParamsInterceptor interceptor;

        public Builder() {
            this.interceptor = new BasicParamsInterceptor();
        }

        public BasicParamsInterceptor.Builder addParam(String key, String value) {
            this.interceptor.paramsMap.put(key, value);
            return this;
        }

        public BasicParamsInterceptor.Builder addParamsMap(Map<String, String> paramsMap) {
            this.interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public BasicParamsInterceptor.Builder addHeaderParam(String key, String value) {
            this.interceptor.headerParamsMap.put(key, value);
            return this;
        }

        public BasicParamsInterceptor.Builder addHeaderParamsMap(Map<String, String> headerParamsMap) {
            this.interceptor.headerParamsMap.putAll(headerParamsMap);
            return this;
        }

        public BasicParamsInterceptor.Builder addHeaderLine(String headerLine) {
            int index = headerLine.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            }
            this.interceptor.headerLinesList.add(headerLine);
            return this;
        }

        public BasicParamsInterceptor.Builder addHeaderLinesList(List<String> headerLinesList) {
            for (String headerLine : headerLinesList) {
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                this.interceptor.headerLinesList.add(headerLine);
            }
            return this;
        }

        public BasicParamsInterceptor.Builder addQueryParam(String key, String value) {
            this.interceptor.queryParamsMap.put(key, value);
            return this;
        }

        public BasicParamsInterceptor.Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
            this.interceptor.queryParamsMap.putAll(queryParamsMap);
            return this;
        }

        public BasicParamsInterceptor build() {
            return this.interceptor;
        }

    }
}