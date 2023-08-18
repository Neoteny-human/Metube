package com.android.metube;

import com.google.gson.annotations.SerializedName;

public class VoteDTO {

    @SerializedName("id")
    private int id;

    @SerializedName("post_id")
    private int postId;

    @SerializedName("content")
    private String content;

    @SerializedName("vote_num")
    private int voteNum;

    //0은 안함, 1은 함.
    @SerializedName("vote")
    private int vote;

    private boolean show;



    @Override
    public String toString() {
        return "VoteDTO{" +
                "id=" + id +
                ", postId=" + postId +
                ", content='" + content + '\'' +
                ", voteNum=" + voteNum +
                ", vote=" + vote +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
