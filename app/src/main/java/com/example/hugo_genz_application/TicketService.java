package com.example.hugo_genz_application;

import retrofit2.http.GET;

public class HugoService {
    @GET("api/profiles/checkin/{id}")
    void checkin()
}
