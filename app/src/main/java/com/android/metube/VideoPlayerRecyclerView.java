package com.android.metube;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;

import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class VideoPlayerRecyclerView extends RecyclerView {

    private static final String TAG = "VideoPlayerRecyclerView";

    //ui
    private ImageView thumbnail, volumeControl;
    private ProgressBar progressBar;
    private View viewHolderParent;
    private FrameLayout frameLayout;

    private PlayerView videoSurfaceView;
    private ExoPlayer videoPlayer;




    //vars
    private ArrayList<VideoDTO> videos = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;

    public VideoPlayerRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VideoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        this.context = context.getApplicationContext();
        Display display = ((WindowManager)
                getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        videoSurfaceView = new PlayerView(this.context);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);


        //player 생성.
//        videoPlayer = new ExoPlayer.Builder(context).build();
        videoPlayer = new ExoPlayer.Builder(context).build();

        //player를 view에 바인드
        videoSurfaceView.setUseController(false);
        videoSurfaceView.setPlayer(videoPlayer);




        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    Log.d(TAG, "onScrollStateChanged: called.");
                    if(thumbnail != null){
                        thumbnail.setVisibility(VISIBLE);
                    }

                    if(!recyclerView.canScrollVertically(1)){
                        playVideo(true);
                    }
                    else{
                        playVideo(false);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if(viewHolderParent != null && viewHolderParent.equals(view)){
                    resetVideoView();
                }
            }
        });


        videoPlayer.addListener(new Player.Listener(){
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);

                switch (playbackState){
                    case Player.STATE_BUFFERING:
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.");

                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: Video ended.");
                        videoPlayer.seekTo(0);
                        break;
                    case Player.STATE_IDLE:
                        Log.d(TAG, "onPlaybackStateChanged: IDLE");
                        break;
                    case Player.STATE_READY:
                        Log.e(TAG, "onPlayerStateChanged: Ready");

                        if(!isVideoViewAdded){
                            addVideoView();
                        }
                        break;
                    default:
                        break;
                }

            }
        });



    }




    public void playVideo(boolean isEndOfList){
        int targetPosition;

        if(!isEndOfList){
//            int startPosition = ((LinearLayoutManager) getLayoutManager())
//                    .findFirstVisibleItemPosition();
//            int endPosition = ((LinearLayoutManager) getLayoutManager())
//                    .findLastVisibleItemPosition();
//
//            //화면에 2개 초과가 떠 있을 시 차이를 1로 만든다.
//            if(endPosition - startPosition > 1){
//                endPosition = startPosition + 1;
//            }
//            //definitely something is wrong. return.
//            if(startPosition < 0 || endPosition < 0){
//                return;
//            }
//
//            //if there is more than 1 list-item on the screen
//            if(startPosition != endPosition) {
//                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
//                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);
//
//                targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
//            } else{
//                targetPosition = startPosition;
//            }
            targetPosition = ((LinearLayoutManager) getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        } else{
            targetPosition = videos.size() - 1;
        }

        Log.d(TAG, "playVideo: target position:" + targetPosition);

        //video is already playing so return
        if(targetPosition == playPosition){
            return;
        }

        //set the position of the list-item that is to be played
        playPosition = targetPosition;


        if(videoSurfaceView == null){
            return;
        }
        //remove any old surface views from previously playing videos
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        int currentPosition = targetPosition
                - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "playVideo: firstvisibleitemposition: "+ ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition());

        View child = getChildAt(currentPosition);
        if (child == null){
            return;
        }

        VideoPlayerViewHolder holder = (VideoPlayerViewHolder) child.getTag();
        if (holder == null){
            playPosition = -1;
            return;
        }

        viewHolderParent = holder.itemView;
        requestManager = holder.requestManager;
        frameLayout = holder.itemView.findViewById(R.id.media_container);
        thumbnail = holder.thumbnail;

        //여기서 홀더에 있는 모든 것들을 대입해줌.

        videoSurfaceView.setPlayer(videoPlayer);

        viewHolderParent.setOnClickListener(videoViewClickListener);


        //미디어아이템들 넣고, prepare and setPlayWhenReady!!
        videoPlayer.removeMediaItem(0);

        MediaItem firstItem = MediaItem.fromUri(videos.get(targetPosition).getVideo());
        videoPlayer.addMediaItem(firstItem);
        videoPlayer.prepare();
        videoPlayer.setPlayWhenReady(true);

    }



    private OnClickListener videoViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    //보이는 비디오영역 리턴
    private int getVisibleVideoSurfaceHeight(int playPosition){
        int at = playPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at:" + at);

        View child = getChildAt(at);
        if(child == null){
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if(location[1] < 0){
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
    }

    //remove the old player
    private void removeVideoView(PlayerView videoView) {
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if(parent == null){
            return;
        }

        int index = parent.indexOfChild(videoView);
        if(index >= 0){
            parent.removeViewAt(index);
            isVideoViewAdded = false;
            viewHolderParent.setOnClickListener(null);
        }
    }
    private void addVideoView(){
        //playerView를 갖다 붙이고,
        frameLayout.addView(videoSurfaceView);
        isVideoViewAdded = true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(VISIBLE);
        videoSurfaceView.setAlpha(1);
        thumbnail.setVisibility(GONE);
    }

    private void resetVideoView(){
        //playerView 제거하고,(뷰만 제거되고 플레이어의 상태는 계속 재생 중 인거지...그래서 끝날 때까지 기다린다...)
        if(isVideoViewAdded){
            removeVideoView(videoSurfaceView);
            playPosition = -1;
            videoSurfaceView.setVisibility(INVISIBLE);
            thumbnail.setVisibility(VISIBLE);
        }
    }

    public void releasePlayer() {
        if(videoPlayer != null){
            videoPlayer.release();
            videoPlayer = null;
        }

        viewHolderParent = null;
    }


    public void setVideos(ArrayList<VideoDTO> videos){
        this.videos = videos;
    }



}
