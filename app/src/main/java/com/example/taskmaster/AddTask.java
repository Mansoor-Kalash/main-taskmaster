package com.example.taskmaster;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmaster.Repo.TaskDao;
import com.example.taskmaster.models.Tasks;

public class AddTask extends AppCompatActivity {
private Button addTask;
private EditText title;
private EditText body;
private EditText status;
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
            status = (EditText) findViewById(R.id.statusE);
            submmit();

            addTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    process();
                }
            });
        }
        public void submmit(){
            AppDatabase appDb = AppDatabase.getInstance(this.getApplicationContext());
            TaskDao taskDao = appDb.taskDao();

            total= taskDao.getAllTasks().size();
            totalTask.setText(taskTotal+total);
        }
public void process(){
    AppDatabase appDb = AppDatabase.getInstance(this.getApplicationContext());

    TaskDao taskDao = appDb.taskDao();
taskDao.insertAll(new Tasks(title.getText().toString(),body.getText().toString(),status.getText().toString()));

submit.setText("submitted");
total= taskDao.getAllTasks().size();
totalTask.setText(taskTotal+total);




}

    }

