package com.android.metube;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    public GoogleSignInClient mGoogleSignInClient;
    MaterialButton revokeBtn, logoutBtn;
    Toolbar toolbar;




    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()//이메일 주소도 요청.
                .requestProfile()
                .build();

        //만든 GoogleSignInOptions를 사용해 GoogleSignInClient 객체를 만든다.
        mGoogleSignInClient = GoogleSignIn.getClient(SignInActivity.this, gso);

        //기존에 로그인 했던 계정을 확인.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(SignInActivity.this);

        //로그인이 되어 있을 경우
        if(gsa != null){
            //...
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(SignInActivity.this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_btn);
//        MaterialButton signOutButton = findViewById(R.id.sign_out_btn);
//        MaterialButton testButton = findViewById(R.id.test_btn);
        revokeBtn = findViewById(R.id.revoke_btn);
        logoutBtn = findViewById(R.id.logout_btn);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        signInButton.setOnClickListener(this);
        revokeBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
//        signOutButton.setOnClickListener(this);
//        testButton.setOnClickListener(this);



    }


    //맨 위 툴바의 메뉴들을 통치하는 함수.
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


    //밑의 버튼들을 통치하는 함수.
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sign_in_btn:
                signIn();
                break;
//            case R.id.sign_out_btn:
//                signOut();
//                break;
            case R.id.revoke_btn:
                revokeAccess();
                break;
            case R.id.logout_btn:
                signOut();
                break;
            default:
                break;
        }
    }



    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        resultLauncher.launch(signInIntent);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //...
                        Intent i = new Intent(SignInActivity.this, MainActivity2.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
    }

    //사용자가 완전한 회원 삭제를 요청한 경우.(사용자가 계정을 삭제하는 경우 앱이 Google API로부터 얻은 정보를 완전히 삭제해야 하므로.
    private void revokeAccess(){
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //...


                    }
                });
    }


    ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Task<GoogleSignInAccount> task =
                                        GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                                handleSignInResult(task);
                                Intent i = new Intent(SignInActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                        }
                    }
            );


    void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                String DisplayName = account.getDisplayName();
                String GivenName = account.getGivenName();
                String FamilyName = account.getFamilyName();
                String Email = account.getEmail();
                String Id = account.getId();
                Uri PhotoUrl = account.getPhotoUrl();

                Log.d("로그인한 유저 전체이름", DisplayName);
                Log.d("로그인한 유저 성", GivenName);
                Log.d("로그인한 유저 이름", FamilyName);
                Log.d("로그인한 유저 이메일", Email);
                Log.d("로그인한 유저 아이디", Id);
                if(PhotoUrl!=null) {
                    Log.d("로그인한 유저 프사주소", PhotoUrl.toString());
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(RegisterInterface.REG_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                RegisterInterface api = retrofit.create(RegisterInterface.class);
                Call<String> call = api.getUserRegist(Email, DisplayName);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            Log.e("onSuccess", response.body());

                            String jsonResponse = response.body();

                            try
                            {
                                Log.d("레트로", "onResponse: 음"+response.body().toString());
                                parseRegData(jsonResponse);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }

                    private void parseRegData(String jsonResponse) throws JSONException {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        if(jsonObject.optString("status").equals("true")){
                            Log.d("레트로핏", "parseRegData: 성공");
                        }
                        else{
                            Log.d("레트로핏", "parseRegData: 실패"+jsonObject.optString("message"));
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("레트로핏", "에러 = " + t.getMessage());
                    }
                });
            }
        } catch (ApiException e) {
            Log.e("레트로핏", "handleSignInResult: " + e.getStatusCode());
        }


    }
}