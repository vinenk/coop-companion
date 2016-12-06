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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ResourceListActivity extends Fragment implements FirebaseDatabaseManager.userObjectListener{
    private static final int REQUESTCODE = 555;
    public ArrayList<Resource> resources;
    private RecyclerView recyclerView;
    private Button addResource;
    protected ResourceListAdapter resourceListAdapter = null;
    protected LinearLayoutManager rv_layout_mgr;
    protected Context context;
    protected FirebaseDatabaseManager fDManager;

    public static ResourceListActivity newInstance(FirebaseDatabaseManager fDManager, Context context){
        ResourceListActivity resourceListActivity = new ResourceListActivity();
        resourceListActivity.setFDManager(fDManager);
        resourceListActivity.context = context;
        return resourceListActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_resource_list);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_resource_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_resource_list);
        addResource = (Button) v.findViewById(R.id.resourceview_add_resource);
        resources = new ArrayList<>();
        fDManager.getCurrentUser(this);
        resourceListAdapter = new ResourceListAdapter(context, resources);
        recyclerView.setAdapter(resourceListAdapter);
        rv_layout_mgr = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(rv_layout_mgr);
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.MYPREFERENCES, MODE_PRIVATE);
        String userJSON = prefs.getString(MainActivity.USER, "");
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(userJSON, User.class);

        addResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ResourceCreateActivity.class);
                intent.putExtra("users", currentUser.getGroupMembers());
                startActivityForResult(intent, REQUESTCODE);
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

        fDManager.getCurrentUser(this);

    }

    public Context getContext() {
        return context;
    }

    public void setFDManager(FirebaseDatabaseManager fDManager){
        this.fDManager = fDManager;
    }

    @Override
    public void adminUserCallback(User admin) {

    }

    @Override
    public void userCallback(User user) {
        fDManager.getResourcesList(user, new FirebaseDatabaseManager.resourceListListener() {
            @Override
            public void resourceListCallback(ArrayList<Resource> rs) {
                setResources(rs);
                Log.d("inResourceList", "2 " + rs.size());
                Log.d("inResourceList", "2 " + resources.size());
            }
        });
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
        this.resourceListAdapter = new ResourceListAdapter(context, resources);
        this.recyclerView.setAdapter(this.resourceListAdapter);
        Log.d("inResourceList", "1 " + resources.size());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE) {
            Resource resource = new Resource(data.getExtras().getString("resourceName"), data.getExtras().getStringArrayList("checkedUsers"),
                    data.getExtras().getBoolean("collection"), data.getExtras().getString("description"));
            fDManager.addResource(resource);
        }
    }
}
