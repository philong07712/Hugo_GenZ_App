package com.example.hugo_genz_application;

public interface TicketListener {
    void onSuccess(int statusCode);
    void onFailed(int statusCode, String str);
}
