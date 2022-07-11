package com.vnpt.room;

import android.content.Context;

import com.vnpt.retrofit.ApiClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import androidx.room.Room;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppDataHelper {

    private static AppDatabase appDatabase;
    private static Retrofit retrofit;

    public static AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "dbasve")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }

    public static Retrofit getRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://blubnd.gialai.vnpt.vn/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public static ApiClient getApiClient() {
        return getRetrofit().create(ApiClient.class);
    }
}
