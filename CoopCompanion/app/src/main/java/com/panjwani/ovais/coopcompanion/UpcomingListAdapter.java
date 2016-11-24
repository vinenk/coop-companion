package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class UpcomingListAdapter extends RecyclerView.Adapter<UpcomingListAdapter.UpcomingListViewHolder> {
    private ArrayList<TaskOrResource> taskOrResources = new ArrayList<>();
    private Context mContext;

    public UpcomingListAdapter(Context context, ArrayList<TaskOrResource> taskOrResources) {
        this.mContext = context;
        this.taskOrResources = taskOrResources;
    }

    public UpcomingListAdapter.UpcomingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_upcoming_list, parent, false);
        UpcomingListAdapter.UpcomingListViewHolder holder = new UpcomingListAdapter.UpcomingListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UpcomingListAdapter.UpcomingListViewHolder holder, int position) {
        TaskOrResource taskOrResource = taskOrResources.get(position);
        holder.upcomingName.setText(taskOrResource.name);
        String peopleAssigned = "";
        for (String person : taskOrResource.peopleAssigned) {
            peopleAssigned += person + ", ";
        }
        peopleAssigned = peopleAssigned.substring(peopleAssigned.length() - 3, peopleAssigned.length() - 1);
        holder.people.setText(peopleAssigned);
        if (taskOrResource.isTask) {
            holder.upcomingDone.setText("Done");
            holder.dueDate.setText(taskOrResource.task.date.toString());
        } else {
            if (taskOrResource.resource.collection) {
                if (taskOrResource.resource.collectionStatus == Resource.CollectionStatus.DEPLETED) {
                    holder.upcomingDone.setText("Bought");
                    holder.status.setText("Depleted");
                }
            }
            if (taskOrResource.resource.singleStatus == Resource.SingleStatus.EMPTY) {
                holder.upcomingDone.setText("Refilled");
                holder.status.setText("Empty");
            }
        }
    }

    @Override
    public int getItemCount() {
        return taskOrResources.size();
    }

    public void removeTask(int position) {

        taskOrResources.remove(position);
        notifyItemRemoved(position);
    }

    private Context getContext() {
        return mContext;
    }

    public class UpcomingListViewHolder extends RecyclerView.ViewHolder{

        public Button upcomingDone;
        public TextView upcomingName;
        public TextView people;
        public TextView dueDate;
        public TextView status;

        public UpcomingListViewHolder(View itemView) {
            super(itemView);
            this.upcomingDone = (Button) itemView.findViewById(R.id.task_or_resource_done);
            this.upcomingName = (TextView) itemView.findViewById(R.id.upcoming_name);
            this.people = (TextView) itemView.findViewById(R.id.upcoming_people);
            this.dueDate = (TextView) itemView.findViewById(R.id.upcoming_due_date);
            this.status = (TextView) itemView.findViewById(R.id.upcoming_status);

        }
    }

}
