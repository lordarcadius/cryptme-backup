package com.team4.cryptme;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

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
        View view = layoutInflater.inflate(R.layout.crypted_list, parent, false);
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

        holder.itemView.setOnClickListener(v -> {

            try {

                Intent openIntent = new Intent(Intent.ACTION_VIEW);
                openIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                openIntent.setData(FileProvider.getUriForFile(holder.fileOption.getContext(), BuildConfig.APPLICATION_ID + ".provider", files[holder.getAdapterPosition()]));
                Intent launchIntent = Intent.createChooser(openIntent, "Open with");
                holder.itemView.getContext().startActivity(launchIntent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(holder.itemView.getContext(), "No application available to open this file.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView fileTypeImage, fileOption;
        public TextView fileName;

        public ViewHolder(View itemView) {
            super(itemView);

            this.fileTypeImage = itemView.findViewById(R.id.fileTypeImage);
            this.fileOption = itemView.findViewById(R.id.fileOptions);
            this.fileName = itemView.findViewById(R.id.fileName);
        }
    }

    public interface ExploreFragmentListener {
        void onFileDelete();
    }
}
