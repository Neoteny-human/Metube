package com.android.metube;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private VideoPlayerRecyclerView2 recyclerView;
    private VideoPlayerRecyclerAdapter adapter;


    ImageView exploreBtn;
    TextView allBtn, gamingBtn, liveBtn, musicBtn, cookingBtn;
    ImageButton userBtn;

    static Context ct;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ct = container.getContext();

        userBtn = view.findViewById(R.id.userBtn);



        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ct, SignInActivity.class);
                startActivity(i);
            }
        });

        //기존에 로그인 했던 계정을 확인.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ct);

        //로그인이 되어 있을 경우
        if (account != null) {
            userBtn.setImageResource(R.drawable.user);
            String TAG = "레트로핏유저";
            ChannelUserInterface api = ApiClient.getApiClient().create(ChannelUserInterface.class);
            Call<ChannelDTO> call = api.getAChannelByEmail(account.getEmail());
            call.enqueue(new Callback<ChannelDTO>() {
                @Override
                public void onResponse(Call<ChannelDTO> call, Response<ChannelDTO> response) {
                    Log.d(TAG, "onResponse: "+response.body());
                    String profile = response.body().getProfile();
                    if(profile!=null){
                        Glide.with(userBtn).load(profile)
                                .override(100, 100)
                                .apply(new RequestOptions().circleCrop())
                                .into(userBtn);
                    }
                }
                @Override
                public void onFailure(Call<ChannelDTO> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });

//            if (account.getPhotoUrl() != null) {
//                Glide.with(userBtn).load(account.getPhotoUrl()).override(100, 100).apply(new RequestOptions().circleCrop()).into(userBtn);
//            }
            userBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ct, ProfileAcitivy.class);
                    startActivity(i);
                }
            });
        }
        else{
            userBtn.setImageResource(R.drawable.no_user);
        }




        exploreBtn = view.findViewById(R.id.explore_btn);
        allBtn = view.findViewById(R.id.all_btn);
        gamingBtn = view.findViewById(R.id.gaming_btn);
        liveBtn = view.findViewById(R.id.live_btn);
        musicBtn = view.findViewById(R.id.music_btn);
        cookingBtn = view.findViewById(R.id.cooking_btn);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allBtn.setTextColor(Color.WHITE);
                allBtn.setBackground(getResources().getDrawable(R.drawable.press_btn));
                gamingBtn.setTextColor(Color.parseColor("#97000000"));
                gamingBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                liveBtn.setTextColor(Color.parseColor("#97000000"));
                liveBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                musicBtn.setTextColor(Color.parseColor("#97000000"));
                musicBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                cookingBtn.setTextColor(Color.parseColor("#97000000"));
                cookingBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));

                String TAG = "레트로핏2";
                VideoInterface api = ApiClient.getApiClient().create(VideoInterface.class);
                Call<ArrayList<VideoDTO>> call = api.getVideos(0);
                call.enqueue(new Callback<ArrayList<VideoDTO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<VideoDTO>> call, Response<ArrayList<VideoDTO>> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        if (recyclerView.thumbnail != null) {
                            recyclerView.thumbnail.setVisibility(View.VISIBLE);
                        }
                        recyclerView.videoSurfaceView.setVisibility(View.INVISIBLE);
                        recyclerView.videoSurfaceView.setPlayer(null);
                        recyclerView.scrollToPosition(0);

                        recyclerView.setVideos(response.body());
                        adapter.setVideos(response.body());
                        adapter.notifyDataSetChanged();

                        recyclerView.playNewVideo();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<VideoDTO>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });


            }
        });

        gamingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamingBtn.setTextColor(Color.WHITE);
                gamingBtn.setBackground(getResources().getDrawable(R.drawable.press_btn));
                allBtn.setTextColor(Color.parseColor("#97000000"));
                allBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                liveBtn.setTextColor(Color.parseColor("#97000000"));
                liveBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                musicBtn.setTextColor(Color.parseColor("#97000000"));
                musicBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                cookingBtn.setTextColor(Color.parseColor("#97000000"));
                cookingBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));

                String TAG = "레트로핏2";
                VideoInterface api = ApiClient.getApiClient().create(VideoInterface.class);
                Call<ArrayList<VideoDTO>> call = api.getVideos(0);
                call.enqueue(new Callback<ArrayList<VideoDTO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<VideoDTO>> call, Response<ArrayList<VideoDTO>> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        if (recyclerView.thumbnail != null) {
                            recyclerView.thumbnail.setVisibility(View.VISIBLE);
                        }
                        recyclerView.videoSurfaceView.setVisibility(View.INVISIBLE);
                        recyclerView.videoSurfaceView.setPlayer(null);
                        recyclerView.scrollToPosition(0);
                        recyclerView.setVideos(response.body());
                        adapter.setVideos(response.body());
                        adapter.notifyDataSetChanged();

                        recyclerView.playNewVideo();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<VideoDTO>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });


            }
        });

        liveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveBtn.setTextColor(Color.WHITE);
                liveBtn.setBackground(getResources().getDrawable(R.drawable.press_btn));
                gamingBtn.setTextColor(Color.parseColor("#97000000"));
                gamingBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                allBtn.setTextColor(Color.parseColor("#97000000"));
                allBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                musicBtn.setTextColor(Color.parseColor("#97000000"));
                musicBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                cookingBtn.setTextColor(Color.parseColor("#97000000"));
                cookingBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));


                String TAG = "레트로핏2";
                VideoInterface api = ApiClient.getApiClient().create(VideoInterface.class);
                Call<ArrayList<VideoDTO>> call = api.getVideos(2);
                call.enqueue(new Callback<ArrayList<VideoDTO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<VideoDTO>> call, Response<ArrayList<VideoDTO>> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        if (recyclerView.thumbnail != null) {
                            recyclerView.thumbnail.setVisibility(View.VISIBLE);
                        }
                        recyclerView.videoSurfaceView.setVisibility(View.INVISIBLE);
                        recyclerView.videoSurfaceView.setPlayer(null);
                        recyclerView.scrollToPosition(0);

                        recyclerView.setVideos(response.body());
                        adapter.setVideos(response.body());
                        adapter.notifyDataSetChanged();

                        recyclerView.playNewVideo();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<VideoDTO>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicBtn.setTextColor(Color.WHITE);
                musicBtn.setBackground(getResources().getDrawable(R.drawable.press_btn));
                gamingBtn.setTextColor(Color.parseColor("#97000000"));
                gamingBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                liveBtn.setTextColor(Color.parseColor("#97000000"));
                liveBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                allBtn.setTextColor(Color.parseColor("#97000000"));
                allBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                cookingBtn.setTextColor(Color.parseColor("#97000000"));
                cookingBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));

                String TAG = "레트로핏2";
                VideoInterface api = ApiClient.getApiClient().create(VideoInterface.class);
                Call<ArrayList<VideoDTO>> call = api.getVideos(3);
                call.enqueue(new Callback<ArrayList<VideoDTO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<VideoDTO>> call, Response<ArrayList<VideoDTO>> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        if (recyclerView.thumbnail != null) {
                            recyclerView.thumbnail.setVisibility(View.VISIBLE);
                        }
                        recyclerView.videoSurfaceView.setVisibility(View.INVISIBLE);
                        recyclerView.videoSurfaceView.setPlayer(null);
                        recyclerView.scrollToPosition(0);

                        recyclerView.setVideos(response.body());
                        adapter.setVideos(response.body());
                        adapter.notifyDataSetChanged();

                        recyclerView.playNewVideo();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<VideoDTO>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        cookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cookingBtn.setTextColor(Color.WHITE);
                cookingBtn.setBackground(getResources().getDrawable(R.drawable.press_btn));
                gamingBtn.setTextColor(Color.parseColor("#97000000"));
                gamingBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                liveBtn.setTextColor(Color.parseColor("#97000000"));
                liveBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                musicBtn.setTextColor(Color.parseColor("#97000000"));
                musicBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));
                allBtn.setTextColor(Color.parseColor("#97000000"));
                allBtn.setBackground(getResources().getDrawable(R.drawable.explore_background));


                String TAG = "레트로핏2";
                VideoInterface api = ApiClient.getApiClient().create(VideoInterface.class);
                Call<ArrayList<VideoDTO>> call = api.getVideos(4);
                call.enqueue(new Callback<ArrayList<VideoDTO>>() {
                    @Override
                    public void onResponse(Call<ArrayList<VideoDTO>> call, Response<ArrayList<VideoDTO>> response) {
                        Log.d(TAG, "onResponse: " + response.body());
                        if (recyclerView.thumbnail != null) {
                            recyclerView.thumbnail.setVisibility(View.VISIBLE);
                        }
                        recyclerView.videoSurfaceView.setVisibility(View.INVISIBLE);
                        recyclerView.videoSurfaceView.setPlayer(null);
                        recyclerView.scrollToPosition(0);

                        recyclerView.setVideos(response.body());
                        adapter.setVideos(response.body());
                        adapter.notifyDataSetChanged();

                        recyclerView.playNewVideo();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<VideoDTO>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });


        recyclerView = view.findViewById(R.id.recycler1);
        initRecyclerView();

        //리사이클러뷰 사이의 간격(높이) 조절이 필요할 때
        RecyclerDecoration heightDecoration = new RecyclerDecoration(20);
        recyclerView.addItemDecoration(heightDecoration);


//        recyclerView.onScrollStateChanged(RecyclerView.SCROLL_STATE_IDLE);


        return view;
    }


    private void initRecyclerView() {
        //생성자로 생성할 때에 자동으로 init(Context context)실행.
        LinearLayoutManager layoutManager = new LinearLayoutManager(ct);
        recyclerView.setLayoutManager(layoutManager);


//        ArrayList<VideoDTO> videos = new ArrayList<>();
//        recyclerView.setVideos(videos);
//        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(videos, initGlide());
//        recyclerView.setAdapter(adapter);


        String TAG = "레트로핏";
        VideoInterface api = ApiClient.getApiClient().create(VideoInterface.class);
        Call<ArrayList<VideoDTO>> call = api.getVideos(0);
        call.enqueue(new Callback<ArrayList<VideoDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<VideoDTO>> call, Response<ArrayList<VideoDTO>> response) {
                Log.d(TAG, "onResponse: " + response.body());
                recyclerView.setVideos(response.body());
                adapter = new VideoPlayerRecyclerAdapter(response.body(), initGlide());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<ArrayList<VideoDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int video_index = VideoPlayerViewHolder.position;

        switch (id){
            case R.id.save_to_playlist:
                //1. 사용자의 플레이리스트를 전부 불러온다. 2. playlist_video에서 불러온 플레이리스트와 해당 동영상이 겹치는 목록을 찾는다.
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ct);
                String TAG = "플레이리스트 레트로핏";
                Log.d(TAG, "onContextItemSelected: "+account);

                if(account==null){
                    Intent i = new Intent(ct, SignInActivity.class);
                    startActivity(i);
                }else {
                    ArrayList<PlaylistDTO> allList = new ArrayList<>();

                    PlaylistInterface api = ApiClient.getApiClient().create(PlaylistInterface.class);
                    Call<ArrayList<PlaylistDTO>> call = api.getPlaylists(account.getEmail(), video_index);
                    call.enqueue(new Callback<ArrayList<PlaylistDTO>>() {
                        @Override
                        public void onResponse(Call<ArrayList<PlaylistDTO>> call, Response<ArrayList<PlaylistDTO>> response) {
                            Log.d(TAG, "onResponse: " + response.body());
                            PlaylistBottomSheetAdapter adapter = new PlaylistBottomSheetAdapter();
                            PlaylistBottomSheetFragment playlistBottomSheetFragment = new PlaylistBottomSheetFragment(adapter);
                            adapter.setItem(response.body());

                            playlistBottomSheetFragment.show(getActivity().getSupportFragmentManager(), "TAG");
                        }

                        @Override
                        public void onFailure(Call<ArrayList<PlaylistDTO>> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                }
                break;



//                PlaylistInterface api = ApiClient.getApiClient().create(PlaylistInterface.class);
//                    Call<ArrayList<PlaylistDTO>> call = api.getCheckPlaylists("ktwjj2828@gmail.com", video_index);
//                    call.enqueue(new Callback<ArrayList<PlaylistDTO>>() {
//                        @Override
//                        public void onResponse(Call<ArrayList<PlaylistDTO>> call, Response<ArrayList<PlaylistDTO>> response) {
//                            Log.d(TAG, "onResponse: " + response.body());
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<ArrayList<PlaylistDTO>> call, Throwable t) {
//                            Log.d(TAG, "onFailure: " + t.getMessage());
//                        }
//                    });
//                }


        }


        return super.onContextItemSelected(item);
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();
//                .placeholder(R.drawable.white_background)
//                .error(R.drawable.white_background);

        return Glide.with(ct)
                .setDefaultRequestOptions(options);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recyclerView != null)
            recyclerView.releasePlayer();
    }

}