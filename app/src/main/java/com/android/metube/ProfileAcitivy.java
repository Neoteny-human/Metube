package com.android.metube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileAcitivy extends AppCompatActivity {
    Button logoutBtn;
    ImageView userImage, myChannelImage;
    TextView userName, myChannelText;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acitivy);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        userImage = findViewById(R.id.user_image);
        userName = findViewById(R.id.user_name);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(ProfileAcitivy.this);


        String TAG = "레트로핏유저";
        ChannelUserInterface api = ApiClient.getApiClient().create(ChannelUserInterface.class);
        Call<ChannelDTO> call = api.getAChannelByEmail(account.getEmail());
        call.enqueue(new Callback<ChannelDTO>() {
            @Override
            public void onResponse(Call<ChannelDTO> call, Response<ChannelDTO> response) {
                Log.d(TAG, "onResponse: "+response.body());
                String profile = response.body().getProfile();
                if(profile!=null){
                    Glide.with(userImage).load(profile)
                            .override(100, 100)
                            .apply(new RequestOptions().circleCrop())
                            .into(userImage);
                }
            }
            @Override
            public void onFailure(Call<ChannelDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
//        if(account.getPhotoUrl()!=null) {
//            Glide.with(userImage).load(account.getPhotoUrl()).override(130, 130).apply(new RequestOptions().circleCrop()).into(userImage);
//        }
        userName.setText(account.getDisplayName());

        logoutBtn = findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileAcitivy.this, SignInActivity.class);
                startActivity(i);
            }
        });


        myChannelText = findViewById(R.id.myChannelText);
        myChannelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //홈으로 가되, 채널 프래그먼트로 프래그먼트를 교체해라.
                Intent i = new Intent(ProfileAcitivy.this, MainActivity2.class);
                i.putExtra("fragment", "channel");
                startActivity(i);
                finish();
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}