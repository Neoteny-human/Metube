package com.android.metube;

import com.google.gson.annotations.SerializedName;

public class ChannelDTO {

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("channel_name")
    private String channelName;

    @SerializedName("profile")
    private String profile;

    @Override
    public String toString() {
        return "ChannelDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", channelName='" + channelName + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
