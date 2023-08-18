package com.android.metube;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeChannelFragment extends Fragment {

    private VideoPlayerRecyclerView2 recyclerView;
    private VideoPlayerRecyclerAdapter adapter;

    ImageView userImage;
    TextView channelName, channelId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeChannelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeChannelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeChannelFragment newInstance(String param1, String param2) {
        HomeChannelFragment fragment = new HomeChannelFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_channel, container, false);
        Bundle bundle = getArguments();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        userImage = view.findViewById(R.id.user_image);
        channelName = view.findViewById(R.id.channel_name);
        channelId = view.findViewById(R.id.channel_id);

        channelName.setText(account.getDisplayName());
        channelId.setText("@"+account.getEmail());

        String TAG = "레트로핏유저";
        ChannelUserInterface api = ApiClient.getApiClient().create(ChannelUserInterface.class);
        Call<ChannelDTO> call = api.getAChannelByEmail(account.getEmail());
        call.enqueue(new Callback<ChannelDTO>() {
            @Override
            public void onResponse(Call<ChannelDTO> call, Response<ChannelDTO> response) {
                Log.d(TAG, "onResponse: "+response.body());
                String profile = response.body().getProfile();
                if(profile!=null){
                    Glide.with(getContext()).load(profile)
                            .load(profile)
                            .circleCrop()
                            .into(userImage);
                }
            }
            @Override
            public void onFailure(Call<ChannelDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
//        Glide.with(getContext()).load(account.getPhotoUrl()).circleCrop().into(userImage);

        recyclerView = view.findViewById(R.id.recycler2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerDecoration heightDecoration = new RecyclerDecoration(20);
        recyclerView.addItemDecoration(heightDecoration);



        VideoInterface api2 = ApiClient.getApiClient().create(VideoInterface.class);
        Call<ArrayList<VideoDTO>> call2 = api2.getUserVideos(account.getEmail());
        call2.enqueue(new Callback<ArrayList<VideoDTO>>() {
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

        return view;
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();
//                .placeholder(R.drawable.white_background)
//                .error(R.drawable.white_background);

        return Glide.with(getContext())
                .setDefaultRequestOptions(options);
    }
}