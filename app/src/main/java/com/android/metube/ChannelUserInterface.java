package com.android.metube;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChannelUserInterface {

    //channelId로부터 채널DTO가져오기.
    @GET("getAChannel.php")
    Call<ChannelDTO> getAChannel(@Query("id") int channelId);

    //email로부터 채널DTO가져오기.
    @GET("getAChannelByEmail.php")
    Call<ChannelDTO> getAChannelByEmail(@Query("email") String email);
}
