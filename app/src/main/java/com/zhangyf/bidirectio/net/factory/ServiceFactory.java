package com.zhangyf.bidirectio.net.factory;


import android.util.Log;

import com.zhangyf.bidirectio.BuildConfig;
import com.zhangyf.bidirectio.net.AppConfig;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created on 2018-03-09 09:33.
 * 网络请求service工厂类
 *
 * @author zyf
 */
public class ServiceFactory {

    public static final String serverUrl = BuildConfig.JAVA_SERVER_URL;

    private static final OkHttpClient sOtherClient = new OkHttpClient.Builder()
            .cache(new Cache(new File(AppConfig.DEFAULT_SAVE_OK_HTTP_PATH), AppConfig.OK_HTTP_CACHE_SIZE))
            .readTimeout(AppConfig.TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(AppConfig.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(BuildConfig.DEBUG ? new HttpLoggingInterceptor(message ->
                    Log.e("zhangyf", "" + "收到响应:" + message)).setLevel(HttpLoggingInterceptor.Level.BODY) :
                            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            )
            .build();

    /**
     * 用于请求服务器接口
     */
    private static final OkHttpClient sClient = new OkHttpClient.Builder()
            .cache(new Cache(new File(AppConfig.DEFAULT_SAVE_OK_HTTP_PATH), AppConfig.OK_HTTP_CACHE_SIZE))
            .readTimeout(AppConfig.TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(AppConfig.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(BuildConfig.DEBUG ? new HttpLoggingInterceptor(message
                    -> Log.e("zhangyf", "" + "收到响应:" + message)).setLevel(HttpLoggingInterceptor.Level.BODY) :
                            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            )
            .addInterceptor(chain -> {
                Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("token", "123456")
                        .build();
                Response originalResponse = chain.proceed(request);
                return originalResponse;
            })
            .build();

    public static <T> T createYZService(Class<T> serviceClazz) {
        return createOauthService(serverUrl, serviceClazz);
    }

    public static <T> T createOauthService(String baseUrl, Class<T> serviceClazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(sClient)
                .baseUrl(baseUrl)
                // 返回Gson解析对象
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(serviceClazz);
    }

    public static <T> T createOauthOtherService(String baseUrl, Class<T> serviceClazz) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(sOtherClient)
                .baseUrl(baseUrl)
                // 返回String
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(serviceClazz);
    }



}
