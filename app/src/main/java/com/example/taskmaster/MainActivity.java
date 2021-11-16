package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button addTask;
private Button allTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTask = (Button) findViewById(R.id.addTask);
        allTask = (Button) findViewById(R.id.allTask);
        Button task1 = (Button) findViewById(R.id.task1);
        Button task2 = (Button) findViewById(R.id.task2);
        Button task3 = (Button) findViewById(R.id.task3);
        Button setting = (Button) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingPage();
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskActivity();
            }
        });
        allTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTaskActivity();
            }
        });
        task1.setOnClickListener(this);
        task2.setOnClickListener(this);
        task3.setOnClickListener(this);

    }
    public void addTaskActivity(){
        Intent intent = new Intent(this,AddTask.class);
        startActivity(intent);
    }
    public void allTaskActivity(){
        Intent intent = new Intent(this,AllTask.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
       Button task = (Button) findViewById(v.getId());
       String title = task.getText().toString();
       openTaskDetails(title);
    }
    public void openTaskDetails(String title){
        Intent intent = new Intent(this,TaskDetailPage.class);
        intent.putExtra("title",title);
        startActivity(intent);
    }
    public void openSettingPage(){
        Intent intent = new Intent(this,SettingsPage.class);
        startActivity(intent);
    }
}