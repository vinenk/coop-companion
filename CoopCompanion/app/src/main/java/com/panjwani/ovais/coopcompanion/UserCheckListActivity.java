package com.panjwani.ovais.coopcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class UserCheckListActivity extends AppCompatActivity implements UserCheckListAdapter.UserCheckListener{

    private ArrayList<String> checkedUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_list);
        ArrayList<String> users = new ArrayList<>();
        checkedUsers = new ArrayList<>();
        ListView userCheckList = (ListView) findViewById(R.id.lv_user_check);
        UserCheckListAdapter restaurantAdapter = new UserCheckListAdapter(this, this, users);
        userCheckList.setAdapter(restaurantAdapter);
        Button submitUsers = (Button) findViewById(R.id.submit_users);
        submitUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskCreateActivity.class);
                intent.putExtra("checkedUsers", checkedUsers);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void UserCheckCallback(String user, boolean isChecked) {
        if (isChecked) {
            checkedUsers.add(user);
        } else {
            checkedUsers.remove(user);
        }
    }
}
