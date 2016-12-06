package com.panjwani.ovais.coopcompanion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TaskCreateActivity extends AppCompatActivity {

    private static final int REQUESTCODE = 222;
    private static ArrayList<String> checkedUsers;
    private static TextView checkedPeople;
    private static long date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        final ArrayList<String> users = getIntent().getStringArrayListExtra("users");

        final EditText taskName = (EditText) findViewById(R.id.create_task_name);
        checkedPeople = (TextView) findViewById(R.id.checked_people);
        Button addPeople = (Button) findViewById(R.id.add_people);
        final TextView dueDate = (TextView) findViewById(R.id.due_date);
        Button addDueDate = (Button) findViewById(R.id.add_due_date);
        final CheckBox repeatWeekly = (CheckBox) findViewById(R.id.repeat_weekly);
        final EditText description = (EditText) findViewById(R.id.description);
        Button submitTask = (Button) findViewById(R.id.submit_task);

        addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserCheckListActivity.class);
                intent.putExtra("users", users);
                startActivityForResult(intent, REQUESTCODE);
            }
        });

        addDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(TaskCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date d = new Date(year, month, dayOfMonth);
                        dueDate.setText(d.toString());
                        date = d.getTime();
                    }
                }, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                dpd.show();
            }
        });

        submitTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
                Bundle b = new Bundle();
                b.putString("taskName", taskName.getText().toString());
                b.putStringArrayList("checkedUsers", checkedUsers);
                b.putLong("date", date);
                b.putBoolean("repeatWeekly", repeatWeekly.isChecked());
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
