package com.hexoncode.cryptit;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.hexoncode.cryptit.fragment.ExploreFragment;

import java.io.File;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.ViewHolder> {

    private File[] files;
    private ExploreFragmentListener exploreFragmentListener;

    public VerticalAdapter(File[] files) {
        this.files = files;
    }

    public VerticalAdapter(File[] files, ExploreFragmentListener exploreFragmentListener) {
        this.files = files;
        this.exploreFragmentListener = exploreFragmentListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.crypted_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = files[position];

        holder.fileName.setText(file.getName());
        holder.fileOption.setOnClickListener(v -> {

            PopupMenu menu = new PopupMenu(v.getContext(), v);
            menu.getMenuInflater().inflate(R.menu.popup_menu_file_options, menu.getMenu());
            menu.setOnMenuItemClickListener(menuItem -> {

                switch (menuItem.getItemId()) {

                    case R.id.share:

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("*/*");
                        shareIntent.setFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                        shareIntent.putExtra(Intent.EXTRA_STREAM,
                                FileProvider.getUriForFile(holder.fileOption.getContext(), BuildConfig.APPLICATION_ID + ".provider", files[holder.getAdapterPosition()]));
                        holder.fileOption.getContext().startActivity(Intent.createChooser(shareIntent, "Share file using"));

                        break;

                    case R.id.delete:

                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.fileOption.getContext());
                        builder.setTitle("Delete")
                                .setMessage("Are you sure you want to delete " + files[holder.getAdapterPosition()].getName() + "? This action cannot be undone.")
                                .setPositiveButton("Delete", ((dialogInterface, i) -> {
                                    files[holder.getAdapterPosition()].delete();
                                    dialogInterface.dismiss();
                                    if (exploreFragmentListener != null) {
                                        exploreFragmentListener.onFileDelete();
                                    }
                                }))
                                .setNegativeButton("Cancel", ((dialogInterface, i) -> dialogInterface.dismiss()))
                                .show();

                        break;

                }

                return true;
            });
            menu.show();

        });
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView fileTypeImage,fileOption;
        public TextView fileName;

        public ViewHolder(View itemView) {
            super(itemView);

            this.fileTypeImage = itemView.findViewById(R.id.fileTypeImage);
            this.fileOption = itemView.findViewById(R.id.fileOptions);
            this.fileName =  itemView.findViewById(R.id.fileName);
        }
    }

    public interface ExploreFragmentListener {
        void onFileDelete();
    }
}
