package com.hexoncode.cryptit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    public TextView toolbarTitle;
    public Toolbar toolbar;
    public ImageView option;
    SharedPreferences prefs;
    public FrameLayout frameLayout;
    public BottomNavigationView bottomNavigationView;
    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        prefs = getApplicationContext().getSharedPreferences("APP_PREFS",MODE_PRIVATE);
        initView();
    }
    public void initView(){
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        option = findViewById(R.id.option);
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,new EncryptFragment()).commit();

        toolbarTitle.setText(Html.fromHtml("<font color=#ffffff>Crypt-IT</font><font color=#40ACFF>.</font>"));

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                if(option.getDrawable().getConstantState()==ContextCompat.getDrawable(HomeActivity.this,R.drawable.ic_option).getConstantState()){
                    fragmentTransaction.replace(R.id.frameLayout,new SettingsFragment()).commit();
                    toolbarTitle.setText(Html.fromHtml("<font color=#40ACFF>Set</font><font color=#ffffff>tings</font>"));
                    option.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this,R.drawable.ic_about));
                }else{
                    fragmentTransaction.replace(R.id.frameLayout,new AboutUsFragment()).commit();
                    toolbarTitle.setText(Html.fromHtml("<font color=#40ACFF>About</font> <font color=#ffffff>Us</font>"));
                    option.setVisibility(View.GONE);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragmentTransaction = fragmentManager.beginTransaction();
                option.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this,R.drawable.ic_option));
                option.setVisibility(View.VISIBLE);
                switch (item.getItemId()){
                    case R.id.encrypt_view:
                        fragmentTransaction.replace(R.id.frameLayout,new EncryptFragment()).commit();
                        toolbarTitle.setText(Html.fromHtml("<font color=#40ACFF>En</font><font color=#ffffff>crypt!</font>"));
                        break;
                    case R.id.decrypt_view:
                        fragmentTransaction.replace(R.id.frameLayout,new DecryptFragment()).commit();
                        toolbarTitle.setText(Html.fromHtml("<font color=#40ACFF>De</font><font color=#ffffff>crypt!</font>"));
                        break;
                    case R.id.explorer_view:
                        fragmentTransaction.replace(R.id.frameLayout,new ExploreFragment()).commit();
                        toolbarTitle.setText(Html.fromHtml("<font color=#40ACFF>Exp</font><font color=#ffffff>lorer</font>"));
                        break;
                }
                return true;
            };
        });
    }
}