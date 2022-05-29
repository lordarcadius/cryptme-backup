package com.team4.cryptme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context context;
    private List<Group> groups;
    private VerticalAdapter.ExploreFragmentListener exploreFragmentListener;

    /*public GroupAdapter(Context context, List<Group> groups) {
        this.context = context;
        this.groups = groups;
    }*/

    public GroupAdapter(Context context, List<Group> groups, VerticalAdapter.ExploreFragmentListener exploreFragmentListener) {
        this.context = context;
        this.groups = groups;
        this.exploreFragmentListener = exploreFragmentListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group group = groups.get(position);

        holder.title.setText(group.getHeading());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setNestedScrollingEnabled(true);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(new VerticalAdapter(group.getFiles(), exploreFragmentListener));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
