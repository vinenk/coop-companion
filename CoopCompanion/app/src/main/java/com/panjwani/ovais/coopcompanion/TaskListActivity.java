package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {
    private ArrayList<Task> tasks;
    private RecyclerView recyclerView;
    private Button addTask;
    protected TaskListAdapter taskListAdapter = null;
    protected LinearLayoutManager rv_layout_mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_task_list);
        addTask = (Button) findViewById(R.id.taskview_add_task);
        tasks = new ArrayList<>();
        taskListAdapter = new TaskListAdapter(this, tasks);
        recyclerView.setAdapter(taskListAdapter);
        rv_layout_mgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_layout_mgr);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskCreateActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

    public Context getContext() {
        return this;
    }
}
