package ru.moskovka.funumber.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private final String BASE_URL = "http://numbersapi.com/";

    private static NetworkService instance;

    private Retrofit retrofit;

    private NetworkService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static NetworkService getInstance(){
        if (instance == null)
            instance = new NetworkService();
        return instance;
    }

    public NumberApi getNumberApi(){
        return retrofit.create(NumberApi.class);
    }
}
