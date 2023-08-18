package com.android.metube;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CommunityPostDTO {



    @SerializedName("id")
    private int id;
    @SerializedName("channel_id")
    private int channelId;
    @SerializedName("content")
    private String content;
    @SerializedName("video_id")
    private int videoId;
//    @SerializedName("vote_list")
//    private ArrayList<String> voteList;
//
//    @SerializedName("vote_number")
//    private ArrayList<String> voteNumber;
    @SerializedName("images")
    private String images;
    @SerializedName("like")
    private int like;
    @SerializedName("dislike")
    private int dislike;
    @SerializedName("upload_time")
    private String uploadDate;

//1번=비디오; 2번=이미지; 3번=글만; 4번=투표;
    @SerializedName("view_type")
    private int viewType;


    @Override
    public String toString() {
        return "CommunityPostDTO{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", content='" + content + '\'' +
                ", videoId=" + videoId +
                ", images='" + images + '\'' +
                ", like=" + like +
                ", dislike=" + dislike +
                ", uploadDate='" + uploadDate + '\'' +
                ", viewType=" + viewType +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

}
