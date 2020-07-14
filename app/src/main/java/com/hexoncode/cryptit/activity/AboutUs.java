package com.hexoncode.cryptit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexoncode.cryptit.R;

public class AboutUs extends AppCompatActivity {

    public TextView madeInIndia, cryptItText, aboutCryptText, aboutDevText;
    public ImageView web, git, insta, twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        madeInIndia = findViewById(R.id.madeInIndia);
        cryptItText = findViewById(R.id.crypt_it_text);
        aboutCryptText = findViewById(R.id.about_crypt_text);
        aboutDevText = findViewById(R.id.crypt_dev_text);
        web = findViewById(R.id.web);
        git = findViewById(R.id.github);
        twitter = findViewById(R.id.twitter);
        insta = findViewById(R.id.insta);

        madeInIndia.setText(Html.fromHtml("<font color=#FFFFFF>Made with ❤ in India By </font><font color=#40ACFFf>Hexon</font><font color=#FFFFFF>Code</font>"));
        cryptItText.setText(Html.fromHtml("<font color=#FFFFFF>Crypt-</font><font color=#40ACFFf>It</font>"));
        aboutDevText.setText(Html.fromHtml("<font color=#FFFFFF>By </font><font color=#40ACFFf>Hexon</font><font color=#FFFFFF>Code</font>"));
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hexoncode.com")));
            }
        });
        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/hexoncode")));
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/hexoncode")));
            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/hexoncode")));
            }
        });
    }
}