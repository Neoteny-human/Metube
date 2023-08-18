package com.android.metube;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelFragment extends Fragment{
    HomeFragment homeFragment;

    HomeChannelFragment homeChannelFragment;
    VideosChannelFragment videosChannelFragment;
    PlaylistsChannelFragment playlistsChannelFragment;
    CommunityChannelFragment communityChannelFragment;
    ChannelsChannelFragment channelsChannelFragment;
    AboutChannelFragment aboutChannelFragment;
    Context ct;

    Toolbar toolbar;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChannelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChannelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChannelFragment newInstance(String param1, String param2) {
        ChannelFragment fragment = new ChannelFragment();
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
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        homeChannelFragment = new HomeChannelFragment();
        videosChannelFragment = new VideosChannelFragment();
        playlistsChannelFragment = new PlaylistsChannelFragment();
        communityChannelFragment = new CommunityChannelFragment();
        channelsChannelFragment = new ChannelsChannelFragment();
        aboutChannelFragment = new AboutChannelFragment();
        ct = container.getContext();
        homeFragment = new HomeFragment();

        toolbar = view.findViewById(R.id.toolbar);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //뒤로가기 띄우기
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //제목
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(account.getDisplayName());

        getChildFragmentManager().beginTransaction().add(R.id.frame, homeChannelFragment).commit();

        TabLayout tab = (TabLayout) view.findViewById(R.id.tab);


        getArguments().getString("email");

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if(position == 0){
                    //retrofit으로 구독자 수, 비디오 수, 비디오 목록 필요.

                    selected = homeChannelFragment;
                }
                else if(position == 1){
                    //retrofit으로 비디오 목록 필요.
                    selected = videosChannelFragment;
                }
                else if(position == 2){
                    //retrofit으로 플레이리스트 필요.
                    selected = playlistsChannelFragment;
                }
                else if(position == 3){
                    selected = communityChannelFragment;
                }
                else if(position == 4){
                    selected = channelsChannelFragment;
                }
                else if(position == 5){
                    //retrofit으로 채널정보 필요.
                    selected = aboutChannelFragment;
                }

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
                Bundle bundle = new Bundle();
                bundle.putString("email", account.getEmail());
                bundle.putString("displayname", account.getDisplayName());
                homeChannelFragment.setArguments(bundle);
                communityChannelFragment.setArguments(bundle);
                getChildFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        return view;
    }


    //맨 위 툴바의 메뉴들을 통치하는 함수.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                ((MainActivity2)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}