package com.android.metube;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VideoInterface {
    @GET("getvideo.php")
    Call<ArrayList<VideoDTO>> getVideos(@Query("category") int category);

    @GET("getvideo.php/")
    Call<ArrayList<VideoDTO>> getAllVideos();

    @GET("getUserVideo.php")
    Call<ArrayList<VideoDTO>> getUserVideos(@Query("email") String email);

    @GET("getAVideo.php")
    Call<VideoDTO> getAVideo(@Query("id") int id);


}
