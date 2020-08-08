package com.gideon.abiririderapp.Interfaces;


import com.gideon.abiririderapp.Model.fcm.FCMResponse;
import com.gideon.abiririderapp.Model.fcm.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
        "Content-Type:application/json",
        "Authorization:key=AAAAoowxxkY:APA91bED4twqAOJiR6vGXaO6srxhWmU4A2J-pU3oDd5becxepwO3-8peCGLqs2hXw_lbEInvsAgV-Z00BTVHZeskngN41o_qaCjFqDTFndTtx6vBA1pnccwgloHE8pSNpJ6FqpsDAPu7"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
