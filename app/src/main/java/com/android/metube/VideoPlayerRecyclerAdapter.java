package com.android.metube;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<VideoDTO> videos;
    private RequestManager requestManager;

    public VideoPlayerRecyclerAdapter(ArrayList<VideoDTO> videos, RequestManager requestManager) {
        this.videos = videos;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoPlayerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_video_item, parent, false)

        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((VideoPlayerViewHolder)holder).onBind(videos.get(position), requestManager);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public ArrayList<VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<VideoDTO> videos) {
        this.videos = videos;
    }


}
