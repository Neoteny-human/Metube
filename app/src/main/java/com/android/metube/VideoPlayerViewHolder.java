package com.android.metube;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    FrameLayout media_container;
    ImageView thumbnail, profile, three_dots;
    TextView title, channel_name, view_count, upload_date;
    View parent;
    RequestManager requestManager;
    public int index;
    static public int position;


    PlayerView pv;



    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        thumbnail = itemView.findViewById(R.id.thumbnail);
        media_container = itemView.findViewById(R.id.media_container);
        title = itemView.findViewById(R.id.title);
        profile = itemView.findViewById(R.id.profile_image);
        channel_name = itemView.findViewById(R.id.channel_name);
        view_count = itemView.findViewById(R.id.view_count);
        upload_date = itemView.findViewById(R.id.upload_date);
        three_dots = itemView.findViewById(R.id.three_dots);

        pv = itemView.findViewById(R.id.playerView);

        three_dots.setOnCreateContextMenuListener(this);
    }

    public void onBind(VideoDTO video, RequestManager requestManager){
        this.requestManager = requestManager;
        parent.setTag(this);
        this.requestManager
                .load(video.getVideo())
                .override(Resources.getSystem().getDisplayMetrics().widthPixels)
                .fitCenter()
                .into(thumbnail);
        this.requestManager
                .load(video.getProfile())
                .override(140,140)
                .fitCenter()
                .circleCrop()
                .into(profile);

        title.setText(video.getTitle());
        channel_name.setText(video.getChannelName());

        view_count.setText(String.valueOf(video.getViewCount()));
        upload_date.setText(video.getUploadDate());
        index = video.getId();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ((Activity)v.getContext()).getMenuInflater().inflate(R.menu.home_video_menu, menu);
        position = index;

    }


}
