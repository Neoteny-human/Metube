package com.android.metube;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PlaylistDTO {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("channel_id")
    private String channelId;

    @SerializedName("private")
    private int prv;

    @SerializedName("cnt")
    private int cnt;

    boolean isSelected;




    public PlaylistDTO(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "PlaylistDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", channelId='" + channelId + '\'' +
                ", prv=" + prv +
                ", cnt=" + cnt +
                ", isSelected=" + isSelected +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getPrv() {
        return prv;
    }

    public void setPrv(int prv) {
        this.prv = prv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
