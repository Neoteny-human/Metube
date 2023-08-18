package com.android.metube;


import com.google.gson.annotations.SerializedName;

public class VideoDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("video")
    private String video;

    @SerializedName("title")
    private String title;

    @SerializedName("profile")
    private String profile;

    @SerializedName("channel_name")
    private String channelName;

    @SerializedName("view_count")
    private int viewCount;

    @SerializedName("upload_time")
    private String uploadDate;

    @Override
    public String toString() {
        return "VideoDTO{" +
                "id=" + id +
                ", video='" + video + '\'' +
                ", title='" + title + '\'' +
                ", profile='" + profile + '\'' +
                ", channelName='" + channelName + '\'' +
                ", viewCount=" + viewCount +
                ", uploadDate='" + uploadDate + '\'' +
                '}';
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
