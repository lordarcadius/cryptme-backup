package com.hexoncode.cryptit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hexoncode.cryptit.R;
import com.hexoncode.cryptit.fragment.OnboardingFragment;
import com.hexoncode.cryptit.util.Utils;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(getColor(R.color.colorPrimaryDark));

        setContentView(R.layout.activity_onboarding);

        //to prevent direct activity launch
        String deviceId = getIntent().getStringExtra("deviceId");
        if (deviceId == null || !deviceId.equals(Utils.getDeviceId(this))) {
            finish();
            return;
        }

        ViewPager viewPager = findViewById(R.id.viewPager);
        DotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        TextView skipText = findViewById(R.id.skipText);
        TextView nextText = findViewById(R.id.nextText);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(OnboardingFragment.newInstance(R.drawable.welcome, "Welcome", getString(R.string.onboarding_welcome)));
        fragments.add(OnboardingFragment.newInstance(R.drawable.encrypto, "Encrypt", getString(R.string.onboarding_encrypt)));
        fragments.add(OnboardingFragment.newInstance(R.drawable.decrypto, "Decrypt", getString(R.string.onboarding_decrypt)));
        fragments.add(OnboardingFragment.newInstance(R.drawable.trusted, "Trust", getString(R.string.onboarding_trust)));

        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == fragments.size() - 1) {
                    nextText.setText("Start");
                } else nextText.setText("Next");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        skipText.setOnClickListener(v -> finishOnboarding());
        nextText.setOnClickListener(v -> {

            int currentSlide = viewPager.getCurrentItem();

            if (currentSlide < fragments.size() - 1) {
                viewPager.setCurrentItem(currentSlide + 1);
            } else finishOnboarding();
        });

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments));
        dotsIndicator.setViewPager(viewPager);

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragments) {
            super(fm, behavior);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private void finishOnboarding() {
        SharedPreferences preferences = getSharedPreferences("app", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("onboarding", true).apply();
        startActivity(new Intent(this, HomeActivity.class)
                .putExtra("deviceId", Utils.getDeviceId(this)));
        finish();
    }

}