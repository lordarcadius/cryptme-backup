package com.hexoncode.cryptit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.hexoncode.cryptit.activity.HomeActivity;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class OnBoardingActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new Step.Builder().setTitle("Welcome")
                .setContent("faucibus scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur vitae nunc sed velit")
                .setBackgroundColor(Color.parseColor("#232637")) // int background color
                .setDrawable(R.drawable.ic_crypt_logo) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle("Title 1")
                .setContent("faucibus scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur vitae nunc sed velit")
                .setBackgroundColor(Color.parseColor("#232637")) // int background color
                .setDrawable(R.drawable.ic_crypt_logo) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle("Title 2")
                .setContent("faucibus scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur vitae nunc sed velit")
                .setBackgroundColor(Color.parseColor("#232637")) // int background color
                .setDrawable(R.drawable.ic_crypt_logo) // int top drawable
                .build());

        addFragment(new Step.Builder().setTitle("Title 2")
                .setContent("faucibus scelerisque eleifend donec pretium vulputate sapien nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur vitae nunc sed velit")
                .setBackgroundColor(Color.parseColor("#232637")) // int background color
                .setDrawable(R.drawable.ic_crypt_logo) // int top drawable
                .build());
    }

    @Override
    public void finishTutorial() {
        startActivity(new Intent(OnBoardingActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}