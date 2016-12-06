package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class UpcomingListActivity extends Fragment {
    private ArrayList<TaskOrResource> tasksOrResources;
    private RecyclerView recyclerView;
    private Button addTask;
    private Button addResource;
    protected UpcomingListAdapter upcomingListAdapter = null;
    protected LinearLayoutManager rv_layout_mgr;
    protected Context context;
    protected FirebaseDatabaseManager fDManager;

    public static UpcomingListActivity newInstance(FirebaseDatabaseManager fDManager, Context context){
        UpcomingListActivity upcomingListActivity = new UpcomingListActivity();
        upcomingListActivity.setFDManager(fDManager);
        upcomingListActivity.context = context;
        return upcomingListActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_upcoming_list);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_upcoming_list, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.rv_upcoming_list);
        addTask = (Button) v.findViewById(R.id.upcomingview_add_task);
        addResource = (Button) v.findViewById(R.id.upcomingview_add_resource);
        tasksOrResources = new ArrayList<>();
        upcomingListAdapter = new UpcomingListAdapter(context, tasksOrResources);
        recyclerView.setAdapter(upcomingListAdapter);
        rv_layout_mgr = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(rv_layout_mgr);
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.MYPREFERENCES, MODE_PRIVATE);
        String userJSON = prefs.getString(MainActivity.USER, "");
        Gson gson = new Gson();
        User currentUser = gson.fromJson(userJSON, User.class);
        if (!currentUser.isAdmin()) {
            addTask.setVisibility(View.INVISIBLE);
        } else {
            addTask.setVisibility(View.VISIBLE);
        }

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskCreateActivity.class);
                getContext().startActivity(intent);
            }
        });
        addResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResourceCreateActivity.class);
                getContext().startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View v = getView();

    }

    @Override
    public void onResume() {
        super.onResume();

        tasksOrResources = new ArrayList<>();
        fDManager.getCurrentUser(new FirebaseDatabaseManager.userObjectListener() {
            @Override
            public void adminUserCallback(User admin) {

            }

            @Override
            public void userCallback(User user) {
                //populate list with task or resource
                fDManager.getResourcesList(user, new FirebaseDatabaseManager.resourceListListener() {
                    @Override
                    public void resourceListCallback(ArrayList<Resource> rs) {
                        for(int i = 0; i< rs.size(); i++){
                            tasksOrResources.add(new TaskOrResource(rs.get(i)));
                        }
                    }
                });

                fDManager.getTasksList(user, new FirebaseDatabaseManager.taskListListener() {
                    @Override
                    public void taskListCallback(ArrayList<Task> ts) {
                        for(int i = 0; i< ts.size(); i++){
                            tasksOrResources.add(new TaskOrResource(ts.get(i)));
                        }
                    }
                });

            }
        });
    }

    public Context getContext() {
        return context;
    }

    public void setFDManager(FirebaseDatabaseManager fDManager){
        this.fDManager = fDManager;
    }

}
