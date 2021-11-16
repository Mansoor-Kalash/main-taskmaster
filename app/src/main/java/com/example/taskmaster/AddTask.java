package com.example.taskmaster;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
    public class AddTask extends AppCompatActivity {
private Button addTask;
private EditText title;
private EditText body;
private TextView submit;
private TextView totalTask;
private Integer total = 0;
private String taskTotal = "Total task: ";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_task);
            title=(EditText)findViewById(R.id.title);
            body = (EditText) findViewById(R.id.body);
            submit = (TextView) findViewById(R.id.submit);
            totalTask = (TextView) findViewById(R.id.total);
            addTask = (Button) findViewById(R.id.addTask);
            addTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    process();
                }
            });
        }
public void process(){
title.setText("");
body.setText("");
submit.setText("submitted");
total+=1;
totalTask.setText(taskTotal+total);




}

    }

