package com.android.metube;

import com.google.android.exoplayer2.C;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {
    String REG_URL = "http://3.37.96.176/";

    @FormUrlEncoded
    @POST("register.php")
    Call<String> getUserRegist(
            @Field("email") String email,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("removeUser.php")
    Call<String> removeUser(
            @Field("email") String email
    );

}
