package com.user.ckapp.retrofit;

import com.user.ckapp.model.NotiRespone;
import com.user.ckapp.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNofication {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=************************************************************************************************************"
            }
    )
    @POST("fcm/send")
    Observable<NotiRespone> sendNofitication(@Body NotiSendData data);
}
