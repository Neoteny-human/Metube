package com.android.metube;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommunityPostInterface {
    @GET("getChannelPosts.php")
    Call<ArrayList<CommunityPostDTO>> getChannelPosts(
            @Query("id") int channelId);

    //해당 포스트의 투표 목록 + 조회한 채널의 투표 여부를 넣어서 서버에서 보내줘야 함.
    @GET("getVote.php")
    Call<ArrayList<VoteDTO>> getVote(
            @Query("channel_id") int channelId,
            @Query("post_id") int postId);


    @GET("isLike.php")
    Call<String> isLike(
            @Query("channel_id") int channelId,
            @Query("post_id") int postId);

    @GET("isDislike.php")
    Call<String> isDislike(
            @Query("channel_id") int channelId,
            @Query("post_id") int postId);











    @FormUrlEncoded
    @POST("postAPost.php")
    Call<String> postAPost(
            @Field("email") String email,
            @Field("content") String content,
            @Field("view_type") int viewType
    );

    @FormUrlEncoded
    @POST("postImage.php")
    Call<String> postImage(
            @Field("post_id") String postId,
            @Field("image") String image,
            @Field("image_order") int imageOrder
    );

    @FormUrlEncoded
    @POST("postVote.php")
    Call<String> postVote(
            @Field("post_id") String postId,
            @Field("content") String content
    );




    /////////////
    @FormUrlEncoded
    @POST("postLike.php")
    Call<String> postLike(
            @Field("post_id") int postId,
            @Field("channel_id") int channelId,
            @Field("plus") String plus);

    @FormUrlEncoded
    @POST("postDislike.php")
    Call<String> postDislike(
            @Field("post_id") int postId,
            @Field("channel_id") int channelId,
            @Field("plus") String plus);


    @FormUrlEncoded
    @POST("cancelVote.php")
    Call<String> cancelVote(
            @Field("channel_id") int channelId,
            @Field("vote_id") int voteId
    );

    @FormUrlEncoded
    @POST("doVote.php")
    Call<String> doVote(
            @Field("channel_id") int channelId,
            @Field("vote_id") int voteId
    );


    @FormUrlEncoded
    @POST("deletePost.php")
    Call<String> deletePost(
            @Field("post_id") int postId);

    @FormUrlEncoded
    @POST("deleteImgPost.php")
    Call<String> deleteImgPost(
            @Field("post_id") int postId);

    @FormUrlEncoded
    @POST("deleteVotePost.php")
    Call<String> deleteVotePost(
            @Field("post_id") int postId);


}
