package com.example.hugo_genz_application;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRUtil {
    public static void checkQRCode(String message, TicketListener listener) {
        String[] strings = message.split("/");
        if (strings.length != 2) {
            listener.onFailed(300, "QR not valid");
            return;
        }
        int id = Integer.parseInt(strings[0]);
        if (id < 1 || id > 20000) {
            listener.onFailed(300, "QR not valid");
        }
        listener.onSuccess(200);
//        int id = Integer.parseInt(strings[0]);
//        // send get request, wait for status return
//        // if status = 200 then it is check success
//        // if status = 404 then check failed
//        TicketService service = RetrofitUtil.getRetrofit().create(TicketService.class);
//        Call<Void> repo = service.checkin(id);
//        repo.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.code() == 200) {
//                    listener.onSuccess(response.code());
//                } else {
//                    listener.onFailed(response.code(), "QR has used");
//                }
//            }
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                listener.onFailed(404, t.getMessage());
//            }
//        });
    }
}
