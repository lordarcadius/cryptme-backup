package com.team4.cryptme;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recent_files_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView fileTypeImage;
        public TextView fileName;
        public LinearLayout fileTileLL;

        public ViewHolder(View itemView) {
            super(itemView);
            this.fileTypeImage = itemView.findViewById(R.id.fileTypeImage);
            this.fileName = itemView.findViewById(R.id.fileName);
            fileTileLL = itemView.findViewById(R.id.fileTileLL);
        }
    }
}

