package com.example.removedragon;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("/2ZUffkI")
    Call<DataList> getInfo();
}
