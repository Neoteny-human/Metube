package com.android.metube;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityChannelFragment extends Fragment {
    Context ct;
    RecyclerView recyclerView;
    ArrayList<CommunityPostDTO> posts;
    CommunityRecyclerAdapter adapter;
    ChannelDTO channel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String email;
    private String name;

    public CommunityChannelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunityChannelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityChannelFragment newInstance(String param1, String param2) {
        CommunityChannelFragment fragment = new CommunityChannelFragment();
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
            email = getArguments().getString("email");
            name = getArguments().getString("displayname");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_channel, container, false);
        ct = container.getContext();

        ChannelUserInterface api = ApiClient.getApiClient().create(ChannelUserInterface.class);
        Call<ChannelDTO> call = api.getAChannelByEmail(email);
        call.enqueue(new Callback<ChannelDTO>() {
            @Override
            public void onResponse(Call<ChannelDTO> call, Response<ChannelDTO> response) {
                channel = response.body();
            }
            @Override
            public void onFailure(Call<ChannelDTO> call, Throwable t) {
            }
        });

        recyclerView = view.findViewById(R.id.community_recyclerview);
        initRecyclerView();

        //리사이클러뷰 사이의 간격(높이) 조절이 필요할 때
            RecyclerDecoration heightDecoration = new RecyclerDecoration(40);
            recyclerView.addItemDecoration(heightDecoration);
        return view;
    }


    private void initRecyclerView(){
        String TAG = "포스트긁어오기";
        CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
        Call<ArrayList<CommunityPostDTO>> call = api.getChannelPosts(7);
        call.enqueue(new Callback<ArrayList<CommunityPostDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<CommunityPostDTO>> call, Response<ArrayList<CommunityPostDTO>> response) {
                Log.d(TAG, "onResponse: "+ response.body());
                posts = response.body();
                LinearLayoutManager layoutManager = new LinearLayoutManager(ct);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new CommunityRecyclerAdapter(posts, channel);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<CommunityPostDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        int pos = item.getOrder();
        CommunityPostDTO post = adapter.getPosts().get(pos);
        int id = post.getId();
        Log.d("아이디 맞냐?", "onContextItemSelected: "+id);
        int viewType = post.getViewType();
        switch (itemId){
            case R.id.delete:
                //deleteImgPost(post_id) => post => 에 딸린 image목록 => 에 딸린 진짜 s3파일들.
                if(viewType == 2){
                    String TAG = "이미지포스트 삭제";
                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                    Call<String> call = api.deleteImgPost(id);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "onResponse: "+ response.body());
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                    posts.remove(pos);
                    adapter.notifyItemRemoved(pos);
                }
                //deleteVotePost(post_id) => post => 에 딸린 vote목록 => 에 딸린 channel_vote목록
                else if(viewType==4){
                    String TAG = "투표포스트 삭제";
                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                    Call<String> call = api.deleteVotePost(id);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "onResponse: "+ response.body());
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                    posts.remove(pos);
                    adapter.notifyItemRemoved(pos);
                }
                //deletePost(post_id) => post
                else{
                    String TAG = "포스트 삭제";
                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                    Call<String> call = api.deletePost(id);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "onResponse: "+ response.body());
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                    posts.remove(pos);
                    adapter.notifyItemRemoved(pos);
                }
                break;
        }
        return super.onContextItemSelected(item);
    }


}