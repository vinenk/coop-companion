package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class ResourceListAdapter extends RecyclerView.Adapter<ResourceListAdapter.ResourceListViewHolder> {
    private ArrayList<Resource> resources = new ArrayList<>();
    private Context mContext;

    public ResourceListAdapter(Context context, ArrayList<Resource> resources) {
        this.mContext = context;
        this.resources = resources;
    }

    public ResourceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_resource_list, parent, false);
        ResourceListViewHolder holder = new ResourceListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ResourceListViewHolder holder, int position) {
        Resource resource = resources.get(position);
        holder.resourceName.setText(resource.name);
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    public void removeResource(int position) {
        resources.remove(position);
        notifyItemRemoved(position);
    }

    private Context getContext() {
        return mContext;
    }

    public class ResourceListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView resourceName;

        public ResourceListViewHolder(View itemView) {
            super(itemView);
            this.resourceName = (TextView) itemView.findViewById(R.id.resource_name);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ResourceInfoActivity.class);
            intent.putExtra("Resource", getAdapterPosition());
            getContext().startActivity(intent);
        }
    }
}
