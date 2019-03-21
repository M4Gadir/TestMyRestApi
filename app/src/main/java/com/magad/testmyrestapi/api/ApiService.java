package com.magad.testmyrestapi.api;

import com.magad.testmyrestapi.models.ResponseItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("top-headlines")
    Call<ResponseItem> getItems(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}
