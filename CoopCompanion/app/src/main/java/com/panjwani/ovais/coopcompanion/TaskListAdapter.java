package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {
    private ArrayList<Task> tasks = new ArrayList<>();
    private Context mContext;

    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        this.mContext = context;
        this.tasks = tasks;
    }

    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_task_list, parent, false);
        TaskListViewHolder holder = new TaskListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskName.setText(task.name);
        String peopleAssigned = "";
        for (String person : task.peopleAssigned) {
            peopleAssigned += person + ", ";
        }
        peopleAssigned = peopleAssigned.substring(peopleAssigned.length()-3, peopleAssigned.length()-1);
        holder.people.setText(peopleAssigned);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void removeTask(int position) {

        tasks.remove(position);
        notifyItemRemoved(position);
    }

    private Context getContext() {
        return mContext;
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder{

        public TextView taskName;
        public TextView people;

        public TaskListViewHolder(View itemView) {
            super(itemView);
            this.taskName = (TextView) itemView.findViewById(R.id.task_name);
            this.people = (TextView) itemView.findViewById(R.id.people);
        }
    }

}
