package com.panjwani.ovais.coopcompanion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ResourceCreateActivity extends AppCompatActivity {

    private static final int REQUESTCODE = 333;
    private static ArrayList<String> checkedUsers;
    private static TextView checkedPeople;
    private static long date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_create);

        final ArrayList<String> users = getIntent().getStringArrayListExtra("users");

        final EditText resourceName = (EditText) findViewById(R.id.create_resource_name);
        checkedPeople = (TextView) findViewById(R.id.checked_people);
        Button addPeople = (Button) findViewById(R.id.add_people);
        final CheckBox collection = (CheckBox) findViewById(R.id.collection);
        final EditText description = (EditText) findViewById(R.id.description);
        Button submitResource = (Button) findViewById(R.id.submit_resource);

        addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserCheckListActivity.class);
                intent.putExtra("users", users);
                startActivityForResult(intent, REQUESTCODE);
            }
        });

        submitResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResourceListActivity.class);
                Bundle b = new Bundle();
                b.putString("resourceName", resourceName.getText().toString());
                b.putStringArrayList("checkedUsers", checkedUsers);
                b.putBoolean("collection", collection.isChecked());
                b.putString("description", description.getText().toString());
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE) {
            checkedUsers = data.getStringArrayListExtra("checkedUsers");
            String checked = "";
            if (checkedUsers.size() != 0) {
                checked = checkedUsers.toString();
                checked = checked.substring(1, checked.length()-1);
            }
            checkedPeople.setText(checked);
        }
    }
}
