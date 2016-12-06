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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TaskListActivity extends Fragment implements FirebaseDatabaseManager.userObjectListener{
    private static final int REQUESTCODE = 444;
    public ArrayList<Task> tasks;
    private RecyclerView recyclerView;
    private Button addTask;
    protected TaskListAdapter taskListAdapter = null;
    protected LinearLayoutManager rv_layout_mgr;
    protected Context context;
    protected FirebaseDatabaseManager fDManager;

    public static TaskListActivity newInstance(FirebaseDatabaseManager fDManager, Context context){
        TaskListActivity taskListActivity = new TaskListActivity();
        taskListActivity.setFDManager(fDManager);
        taskListActivity.context = context;
        return taskListActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_task_list);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_task_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_task_list);
        addTask = (Button) v.findViewById(R.id.taskview_add_task);
        tasks = new ArrayList<>();
        fDManager.getCurrentUser(this);
        taskListAdapter = new TaskListAdapter(context, tasks);
        recyclerView.setAdapter(taskListAdapter);
        rv_layout_mgr = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(rv_layout_mgr);
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.MYPREFERENCES, MODE_PRIVATE);
        String userJSON = prefs.getString(MainActivity.USER, "");
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(userJSON, User.class);
        if (!currentUser.isAdmin()) {
            addTask.setVisibility(View.INVISIBLE);
        } else {
            addTask.setVisibility(View.VISIBLE);
        }

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TaskCreateActivity.class);
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
        fDManager.getTasksList(user, new FirebaseDatabaseManager.taskListListener() {
            @Override
            public void taskListCallback(ArrayList<Task> ts) {
                setTasks(ts);
                Log.d("inTasksList", "2 " + ts.size());
                Log.d("inTasksList", "2 " + tasks.size());
            }
        });
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.taskListAdapter = new TaskListAdapter(context, tasks);
        this.recyclerView.setAdapter(this.taskListAdapter);
        Log.d("inTasksList", "1 " + this.tasks.size());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE) {
            Task task = new Task(data.getExtras().getString("taskName"), data.getExtras().getStringArrayList("checkedUsers"), data.getExtras().getLong("date"),
                    data.getExtras().getBoolean("repeatWeekly"), data.getExtras().getString("description"));
            fDManager.addTask(task);
        }
    }
}
