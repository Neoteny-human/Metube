package com.android.metube;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlaylistInterface {

    @GET("getPlaylist.php")
    Call<ArrayList<PlaylistDTO>> getPlaylists(
            @Query("email") String email,
            @Query("video_id") int video_id);


    @FormUrlEncoded
    @POST("makePlaylist.php")
    Call<String> makePlaylist(
            @Field("email") String email,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST("postPlaylistVideo.php")
    Call<String> postPlaylistVideo(
            @Field("video_id") int video_id,
            @Field("playlist_id") int playlist_id,
            @Field("connect") boolean connect
    );









    //동영상 index와 email을 보내주고,
    @GET("getPlaylistCheck.php")
    Call<ArrayList<PlaylistDTO>> getCheckPlaylists(
            @Query("email") String email,
            @Query("video_id") int video_id);

}
