package com.example.hugo_genz_application;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TicketService {
    @GET("api/profiles/checkin/{id}")
    Call<Void> checkin(@Path("id") int id);
}
