package com.android.metube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity2 extends AppCompatActivity {

    HomeFragment fragment_home;
    SubscriptionFragment fragment_subscription;
    ChannelFragment fragment_channel;

    BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNavigationView = findViewById(R.id.navBar);

//        프래그먼트 생성.
        fragment_home = new HomeFragment();
        fragment_subscription = new SubscriptionFragment();
        fragment_channel = new ChannelFragment();


        if(getIntent().getStringExtra("fragment") != null){
            Bundle bundle = new Bundle();
            bundle.putString("email", "ktwjj2828@gmail.com");
            fragment_channel.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment_channel).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_home).addToBackStack(null).commit();
        }

        bottomNavigationView.getMenu().clear();

        //기존에 로그인 했던 계정을 확인.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(MainActivity2.this);

        //로그인이 되어 있을 경우
        if(gsa != null){
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);
        }
        else{
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_no_login);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.page_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_home).commit();
                        return true;
                    case R.id.page_subscription:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_subscription).commit();
                        return true;
                    case R.id.create:
                        CreateBottomSheetFragment createBottomSheetFragment = new CreateBottomSheetFragment();
                        createBottomSheetFragment.show(getSupportFragmentManager(),"TAG");
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


}