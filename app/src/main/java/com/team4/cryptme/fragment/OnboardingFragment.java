package com.team4.cryptme.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team4.cryptme.R;

public class OnboardingFragment extends Fragment {

    private Context context;

    public OnboardingFragment() {
        // Required empty public constructor
    }

    public static OnboardingFragment newInstance(int image, String title, String message) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt("image", image);
        args.putString("title", title);
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_onboarding, container, false);

        Bundle bundle = getArguments();

        int image = bundle.getInt("image");
        String title = bundle.getString("title");
        String message = bundle.getString("message");

        ImageView imageView = root.findViewById(R.id.image);
        TextView titleView = root.findViewById(R.id.title);
        TextView messageView = root.findViewById(R.id.message);

        Glide.with(context).load(image).into(imageView);
        titleView.setText(title);
        messageView.setText(message);

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}