package ru.moskovka.funumber.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NumberApi {
    @GET("random/date")
    Call<ResponseBody> getDateRandomFact();

    @GET("random/math")
    Call<ResponseBody> getMathRandomFact();

    @GET("random/trivia")
    Call<ResponseBody> getTriviaRandomFact();

    @GET("random/year")
    Call<ResponseBody> getYearRandomFact();
}
