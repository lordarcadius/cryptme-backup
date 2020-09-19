package com.hexoncode.cryptit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexoncode.cryptit.R;

public class AboutUs extends AppCompatActivity {

    public TextView madeInIndia, cryptItText, aboutDevText;
    public ImageView web, linkedin, facebook, instagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(getColor(R.color.colorPrimaryDark));

        setContentView(R.layout.activity_about_us);
        madeInIndia = findViewById(R.id.madeInIndia);
        cryptItText = findViewById(R.id.crypt_it_text);
        aboutDevText = findViewById(R.id.crypt_dev_text);
        web = findViewById(R.id.web);
        linkedin = findViewById(R.id.linkedin);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);

        madeInIndia.setText("Made with ❤ in India by Hexoncode");
        cryptItText.setText(R.string.app_name);

        web.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hexoncode.com"))));
        linkedin.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/company/hexoncode"))));
        facebook.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/hexoncode"))));
        instagram.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/hexoncode"))));

        findViewById(R.id.donateButton).setOnClickListener(v -> startActivity(new Intent(this, DonationActivity.class)));

    }
}