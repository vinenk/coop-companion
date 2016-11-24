package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class UpcomingListActivity extends AppCompatActivity {
    private ArrayList<TaskOrResource> tasksOrResources;
    private RecyclerView recyclerView;
    private Button addTask;
    private Button addResource;
    protected UpcomingListAdapter upcomingListAdapter = null;
    protected LinearLayoutManager rv_layout_mgr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_upcoming_list);
        addTask = (Button) findViewById(R.id.upcomingview_add_task);
        addResource = (Button) findViewById(R.id.upcomingview_add_resource);
        tasksOrResources = new ArrayList<>();
        upcomingListAdapter = new UpcomingListAdapter(this, tasksOrResources);
        recyclerView.setAdapter(upcomingListAdapter);
        rv_layout_mgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_layout_mgr);

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
    }

    public Context getContext() {
        return this;
    }
}
