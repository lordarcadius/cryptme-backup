package com.hexoncode.cryptit;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ExploreFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EditText searchBar;
    public LinearLayout viewAll;
    public RecyclerView recentRV, encryptedRV, decryptedRV;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);
        searchBar = view.findViewById(R.id.searchBar);
        viewAll = view.findViewById(R.id.viewAll);
        recentRV = view.findViewById(R.id.recentRV);
        encryptedRV = view.findViewById(R.id.encryptedRV);
        decryptedRV = view.findViewById(R.id.decryptedRV);

        HorizontalAdapter horizontalAdapter = new HorizontalAdapter();
        recentRV.setHasFixedSize(true);
        recentRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
        recentRV.setAdapter(horizontalAdapter);

        VerticalAdapter verticalAdapter = new VerticalAdapter();
        encryptedRV.setHasFixedSize(true);
        encryptedRV.setLayoutManager(new LinearLayoutManager(getContext()));
        encryptedRV.setAdapter(verticalAdapter);

        decryptedRV.setHasFixedSize(true);
        decryptedRV.setLayoutManager(new LinearLayoutManager(getContext()));
        decryptedRV.setAdapter(verticalAdapter);

        searchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                        Toast.makeText(getContext(), searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });


        return view;
    }
}