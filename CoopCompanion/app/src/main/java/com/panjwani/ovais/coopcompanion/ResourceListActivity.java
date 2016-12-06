package com.panjwani.ovais.coopcompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ResourceListActivity extends AppCompatActivity {
    private ArrayList<Resource> resources;
    private RecyclerView recyclerView;
    private Button addResource;
    protected ResourceListAdapter resourceListAdapter = null;
    protected LinearLayoutManager rv_layout_mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_resource_list);
        addResource = (Button) findViewById(R.id.resourceview_add_resource);
        resources = new ArrayList<>();
        resourceListAdapter = new ResourceListAdapter(this, resources);
        recyclerView.setAdapter(resourceListAdapter);
        rv_layout_mgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rv_layout_mgr);
        SharedPreferences prefs = getSharedPreferences(MainActivity.MYPREFERENCES, MODE_PRIVATE);
        String userJSON = prefs.getString(MainActivity.USER, "");
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(userJSON, User.class);

        addResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResourceCreateActivity.class);
                intent.putExtra("users", currentUser.getGroupMembers());
                getContext().startActivity(intent);
            }
        });
    }

    public Context getContext() {
        return this;
    }
}
