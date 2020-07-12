package com.hexoncode.cryptit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public TextView madeInIndia, cryptItText, aboutCryptText, aboutDevText;
    public ImageView web, git, insta, twitter;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_about_us, container, false);
        madeInIndia = view.findViewById(R.id.madeInIndia);
        cryptItText = view.findViewById(R.id.crypt_it_text);
        aboutCryptText = view.findViewById(R.id.about_crypt_text);
        aboutDevText = view.findViewById(R.id.crypt_dev_text);
        web = view.findViewById(R.id.web);
        git = view.findViewById(R.id.github);
        twitter = view.findViewById(R.id.twitter);
        insta = view.findViewById(R.id.insta);

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
        return view;
    }
}