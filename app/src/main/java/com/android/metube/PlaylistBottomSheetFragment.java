package com.android.metube;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistBottomSheetFragment extends BottomSheetDialogFragment {
    PlaylistBottomSheetAdapter adapter;
    TextView newPlaylist;
    ImageButton doneBtn;

    public PlaylistBottomSheetFragment(PlaylistBottomSheetAdapter adapter) {
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.playlist_bottomsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //어댑터를 연결.
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setAdapter(adapter);


        doneBtn = view.findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        newPlaylist = view.findViewById(R.id.new_playlist);
        newPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이어로그를 띄우고, 거기서 플리추가할 수 있게.
                final EditText editText = new EditText(getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New playlist");
                builder.setView(editText);
                builder.setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
                                String TAG = "플레이리스트2 레트로핏";
                                if (account == null) {
                                    Intent i = new Intent(getContext(), SignInActivity.class);
                                    startActivity(i);
                                } else {
                                    PlaylistInterface api = ApiClient.getApiClient().create(PlaylistInterface.class);
                                    Call<String> call = api.makePlaylist(account.getEmail(), String.valueOf(editText.getText()));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            Log.d(TAG, "onResponse: " + response.body());
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d(TAG, "onFailure: " + t.getMessage());
                                        }
                                    });
                                }
                                dismiss();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        });

                builder.show();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        int video_index = VideoPlayerViewHolder.position;

        for (int i = 0; i < adapter.getItemCount(); i++) {
            String TAG = "플레이리스트2 레트로핏";
            PlaylistInterface api = ApiClient.getApiClient().create(PlaylistInterface.class);
            Call<String> call = api.postPlaylistVideo(video_index, adapter.playlists.get(i).getId(),
                    adapter.playlists.get(i).isSelected());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "onResponse: " + response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }
}
