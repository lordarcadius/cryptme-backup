package com.hexoncode.cryptit.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.FileObserver;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hexoncode.cryptit.Group;
import com.hexoncode.cryptit.GroupAdapter;
import com.hexoncode.cryptit.HorizontalAdapter;
import com.hexoncode.cryptit.R;
import com.hexoncode.cryptit.VerticalAdapter;
import com.hexoncode.cryptit.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExploreFragment extends Fragment implements VerticalAdapter.ExploreFragmentListener {

    private Context context;
    private List<File> allFiles = new ArrayList<>();

    public EditText searchBar;
    private RecyclerView recyclerView;
    private View nullLayout;
    private View progressBar;
    private RecyclerView searchRecyclerView;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onFileDelete() {
        loadData();
        if (searchBar != null) {
            searchBar.setText("");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_explore, container, false);
        searchBar = root.findViewById(R.id.searchBar);
        recyclerView = root.findViewById(R.id.recyclerView);
        nullLayout = root.findViewById(R.id.nullLayout);
        progressBar = root.findViewById(R.id.progressBar);
        searchRecyclerView = root.findViewById(R.id.searchRecyclerView);

        File encryptedFilesDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Crypt-It" + File.separator + "Encrypted");
        File decryptedFilesDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Crypt-It" + File.separator + "Decrypted");

        if (!encryptedFilesDirectory.exists()) encryptedFilesDirectory.mkdirs();
        if (!decryptedFilesDirectory.exists()) decryptedFilesDirectory.mkdirs();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                int spacing = (int) Utils.pxFromDp(context, 8);
                int position = parent.getChildAdapterPosition(view);
                int count = state.getItemCount();

                outRect.top = position == 0 ? spacing : spacing / 2;
                outRect.bottom = position == count - 1 ? spacing : spacing / 2;
            }
        });

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        searchRecyclerView.setHasFixedSize(true);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String keyword = searchBar.getText().toString().trim();
                if (keyword.length() > 0) {

                    List<File> searchedFiles = new ArrayList<>();
                    for (File f : allFiles) {
                        if (f.getName().contains(keyword)) {
                            searchedFiles.add(f);
                        }
                    }

                    searchRecyclerView.setAdapter(new VerticalAdapter(searchedFiles.toArray(new File[0]), ExploreFragment.this));

                    if (searchRecyclerView.getVisibility() != View.VISIBLE) {
                        searchRecyclerView.setVisibility(View.VISIBLE);
                    }

                } else {

                    if (searchRecyclerView.getVisibility() == View.VISIBLE) {
                        searchRecyclerView.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadData();

        return root;
    }

    public void loadData() {

        allFiles.clear();

        new GetFilesTask(new GetFilesListener() {
            @Override
            public void onLoading() {
                showLoading();
            }

            @Override
            public void onLoad(File[] encryptedFiles, File[] decryptedFiles) {

                if (encryptedFiles != null && decryptedFiles != null) {

                    allFiles.addAll(Arrays.asList(encryptedFiles));
                    allFiles.addAll(Arrays.asList(decryptedFiles));

                    Group encryptedGroup = new Group("Encrypted files", encryptedFiles);
                    Group decryptedGroup = new Group("Decrypted files", decryptedFiles);

                    List<Group> groups = new ArrayList<>();
                    if (encryptedFiles.length > 0) {
                        groups.add(encryptedGroup);
                    }
                    if (decryptedFiles.length > 0) {
                        groups.add(decryptedGroup);
                    }

                    if (groups.size() == 0) {
                        showNullLayout();
                    } else {
                        recyclerView.setAdapter(new GroupAdapter(context, groups, ExploreFragment.this));
                        showRecycler();
                    }

                } else showNullLayout();

            }
        }).execute();

    }

    private void showRecycler() {
        recyclerView.setVisibility(View.VISIBLE);
        nullLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showNullLayout() {
        recyclerView.setVisibility(View.GONE);
        nullLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        recyclerView.setVisibility(View.GONE);
        nullLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    class GetFilesTask extends AsyncTask<Void, Void, List<File[]>> {

        GetFilesListener listener;

        public GetFilesTask(GetFilesListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onLoading();
        }

        @Override
        protected List<File[]> doInBackground(Void... voids) {

            File encryptedFilesDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Crypt-It" + File.separator + "Encrypted");
            File decryptedFilesDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Crypt-It" + File.separator + "Decrypted");

            File[] encryptedFiles = encryptedFilesDirectory.listFiles();
            File[] decryptedFiles = decryptedFilesDirectory.listFiles();

            //sorting encrypted files based on extension and type
            if (encryptedFiles != null) {
                ArrayList<File> list = new ArrayList<>(Arrays.asList(encryptedFiles));
                Iterator<File> iterator = list.iterator();
                while (iterator.hasNext()) {
                    File f = iterator.next();
                    if (!f.getName().endsWith(".aes") || !f.isFile()) {
                        list.remove(f);
                    }
                }

                encryptedFiles = list.toArray(new File[0]);
            }

            //sorting decrypted files based on type
            if (decryptedFiles != null) {
                ArrayList<File> list = new ArrayList<>(Arrays.asList(decryptedFiles));
                Iterator<File> iterator = list.iterator();
                while (iterator.hasNext()) {
                    File f = iterator.next();
                    if (!f.isFile()) {
                        list.remove(f);
                    }
                }

                decryptedFiles = list.toArray(new File[0]);
            }

            return Arrays.asList(encryptedFiles, decryptedFiles);
        }

        @Override
        protected void onPostExecute(List<File[]> allFiles) {
            super.onPostExecute(allFiles);
            listener.onLoad(allFiles.get(0), allFiles.get(1));
        }
    }

    interface GetFilesListener {
        void onLoading();
        void onLoad(File[] encryptedFiles, File[] decryptedFiles);
    }

}