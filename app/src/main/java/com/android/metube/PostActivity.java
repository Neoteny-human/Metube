package com.android.metube;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import okio.BufferedSink;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {
    EditText content;
    ImageView getImages, createVote;
    TextView postButton, addVote;
    RecyclerView imageRecycler, voteRecycler;
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<VoteDTO> voteList = new ArrayList<>();
    PostImageAdapter imageAdapter;
    PostVoteAdapter voteAdapter;

    String post_id;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        content = findViewById(R.id.content);
        getImages = findViewById(R.id.get_images);
        createVote = findViewById(R.id.create_vote);
        postButton = findViewById(R.id.post_button);
        imageRecycler = findViewById(R.id.image_recycler);
        voteRecycler = findViewById(R.id.vote_recycler);
        addVote = findViewById(R.id.add_vote);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기 띄우기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //제목
        getSupportActionBar().setTitle("Posting");

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(PostActivity.this);

        imageAdapter = new PostImageAdapter(uriList, this);
        imageAdapter.setOnItemClickListener(new PostImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                uriList.remove(position);
                imageAdapter.notifyItemRemoved(position);
            }
        });
        imageRecycler.setAdapter(imageAdapter);   // 리사이클러뷰에 어댑터 세팅
        imageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));


        voteAdapter = new PostVoteAdapter(voteList, this);
        voteAdapter.setOnItemClickListener(new PostVoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                voteList.remove(position);
                voteAdapter.notifyItemRemoved(position);
            }
        });
        voteRecycler.setAdapter(voteAdapter);
        voteRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));





        createVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageRecycler.setVisibility(View.GONE);
                createVote.setVisibility(View.GONE);
                getImages.setVisibility(View.GONE);
                voteRecycler.setVisibility(View.VISIBLE);
                addVote.setVisibility(View.VISIBLE);
                voteList.add(voteList.size(), new VoteDTO());
                voteAdapter.notifyItemInserted(voteList.size());

                addVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voteList.add(voteList.size(), new VoteDTO());
                        voteAdapter.notifyItemInserted(voteList.size());
                        for(int i = 0; i < voteList.size(); i++) {
                            Log.d("확인용", "onClick: " + voteList.get(i).getContent());
                        }
                    }
                });
            }
        });

        getImages.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                voteRecycler.setVisibility(View.GONE);
                createVote.setVisibility(View.GONE);
                getImages.setVisibility(View.GONE);
                getImages.setVisibility(View.VISIBLE);
                imageRecycler.setVisibility(View.VISIBLE);
                try{
                    if(
                            ActivityCompat.checkSelfPermission(PostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED ||
                                    ActivityCompat.checkSelfPermission(PostActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                    ){
                        ActivityCompat.requestPermissions(PostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3333);
                    }

                else{
                    Intent i = new Intent(Intent.ACTION_PICK);
//                    i.setType("video/*");
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 2222);
                }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TAG = "레트로핏2포스트";
                int viewType = 3;
                //포스팅 (이미지형)
                if(uriList.size()>0){
                    viewType = 2;
                    AWSCredentials awsCredentials = new BasicAWSCredentials("AKIA2N3JELS7ZWGK46UY", "PgWxVU8ona51jGiIyknHjY5LC2aGnGFO/WUgp0Yz");    // IAM 생성하며 받은 것 입력
                    AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));
                    ArrayList<File> fileToUpload = TempFileList(uriList);
                    Log.d("여기", "onClick: ");
//                for (int i = 0; i < uriList.size(); i++) {
//                    fileToUpload.add(new File(getPathFromUri(uriList.get(i))));
//                    Log.d("파일 경로", "onClick: "+getPathFromUri(uriList.get(i)));
//                }
                    TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(PostActivity.this).build();
                    TransferNetworkLossHandler.getInstance(PostActivity.this);

                    for(File file: fileToUpload) {

                        TransferObserver uploadObserver = transferUtility.upload("metube-bucket/image", file.getName(), file);    // (bucket api, file이름, file객체)

                        Log.d("파일 이름", "onClick: "+file.getName());

                        uploadObserver.setTransferListener(new TransferListener() {
                            @Override
                            public void onStateChanged(int id, TransferState state) {
                                if (state == TransferState.COMPLETED) {
                                    // Handle a completed upload
                                }
                            }
                            @Override
                            public void onProgressChanged(int id, long current, long total) {
                                int done = (int) (((double) current / total) * 100.0);
                                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done");
                            }
                            @Override
                            public void onError(int id, Exception ex) {
                                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX:" + ex.toString());
                            }
                        });
                    }
                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                    Call<String> call = api.postAPost(account.getEmail(), content.getText().toString(), viewType);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "포스트"+response.body());
                            post_id = response.body();
                            for (int i = 0; i < fileToUpload.size(); i++) {
                                Call<String> call2 = api.postImage(post_id, fileToUpload.get(i).getName(), i+1);
                                call2.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Log.d(TAG, "이미지"+response.body());
                                    }
                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d(TAG, "onFailure: " + t.getMessage());
                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                }else if(voteList.size()>0){
                    //포스팅 (투표형)
                    viewType = 4;
                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                    Call<String> call = api.postAPost(account.getEmail(), content.getText().toString(), viewType);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "포스트"+response.body());
                            post_id = response.body();
                            for (int i = 0; i < voteList.size(); i++) {
                                Call<String> call2 = api.postVote(post_id, voteList.get(i).getContent());
                                call2.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Log.d(TAG, "투표"+response.body());
                                    }
                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d(TAG, "onFailure: " + t.getMessage());
                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                } else{
                    //포스팅 (민짜형)
                    CommunityPostInterface api = ApiClient.getApiClient().create(CommunityPostInterface.class);
                    Call<String> call = api.postAPost(account.getEmail(), content.getText().toString(), viewType);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "포스트"+response.body());
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });
                }

                finish();
            }
        });

    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        } else {   // 이미지를 하나라도 선택한 경우
            if (data.getClipData() == null) {     // 이미지를 하나만 선택한 경우
//                Log.e("패쓰 ", getPathFromUri(data.getData()));
                Uri imageUri = data.getData();
                Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);
                cursor.moveToNext();
                Log.e("패쓰", "onActivityResult: ");
                cursor.close();
                uriList.add(imageUri);
                imageAdapter.notifyDataSetChanged();
            } else {      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
//                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if (clipData.getItemCount() > 10) {   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                } else {   // 선택한 이미지가 1장 이상 10장 이하인 경우

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri(); // 선택한 이미지들의 uri를 가져온다.
//                        Log.e("패쓰 ", getPathFromUri(data.getData()));
                        Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);
                        cursor.moveToNext();
                            Log.e("패쓰", "onActivityResult: " + cursor.getString(2));
                            cursor.close();
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.
                        } catch (Exception e) {
                            Log.e("TAG", "File select error", e);
                        }
                    }
                    imageAdapter.notifyDataSetChanged();

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 3333:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    public String getPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        @SuppressLint("Range")
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();
        return path;
    }


    public ArrayList<File> TempFileList(ArrayList<Uri> uriList) {
        String TAG = "임시파일";
        ArrayList<File> tempFileList = new ArrayList<>();
        long timestamp = System.currentTimeMillis();
        for(int i = 0; i < uriList.size(); i++) {
            try (InputStream in = new FileInputStream(getPathFromUri(uriList.get(i)))) {
                File tempFile = new File("/storage/emulated/0/Download", String.valueOf(timestamp) +i+ ".png");
                Log.d(TAG, "TempFileList: 3"+tempFile.getName());
                if (tempFile.createNewFile() == true) {
                    Log.d(TAG, "TempFileList: " + tempFile.getName());
                    try (OutputStream out = new FileOutputStream(tempFile)) {
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        tempFileList.add(tempFile);
                    }
                }
                else{
                    Log.d(TAG, "TempFileList: 파일이 이미 존재");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//            Bitmap bitmap = BitmapFactory.decodeStream(in);
//            File tempFile = new File("/storage/emulated/0/Download", String.valueOf(timestamp)+".png");
//            tempFile.createNewFile();
//            Log.d(TAG, "TempFileList: "+tempFile.getName());
//            FileOutputStream out = new FileOutputStream(tempFile);
//            bitmap.compress(Bitmap.CompressFormat.PNG,100, out);
//            out.close();
//            tempFileList.add(tempFile);

            return tempFileList;
    }

    //맨 위 툴바의 메뉴들을 통치하는 함수.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }






}