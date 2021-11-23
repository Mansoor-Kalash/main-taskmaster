package com.example.taskmaster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.Repo.TaskDao;
import com.example.taskmaster.models.Tasks;

import java.util.ArrayList;

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
//            AppDatabase appDb = AppDatabase.getInstance(this.getApplicationContext());
//            TaskDao taskDao = appDb.taskDao();

//            total= taskDao.getAllTasks().size();
            total = 0;
            ArrayList<Task> listTask = new ArrayList<>();
            Amplify.API.query(
                    ModelQuery.list(Task.class, ModelPagination.limit(1_000)),
                    response -> {
                        for (Task task : response.getData()) {
                            total++;
                            listTask.add(task);
                            Log.i("MyAmplifyApp", task.toString());
                        }
                    },
                    error -> Log.e("MyAmplifyApp", "Query failure", error)
            );
            totalTask.setText(taskTotal+total);
        }
public void process(){
//    AppDatabase appDb = AppDatabase.getInstance(this.getApplicationContext());
//
//    TaskDao taskDao = appDb.taskDao();
//taskDao.insertAll(new Tasks(title.getText().toString(),body.getText().toString(),status.getText().toString()));
    Task tasks = Task.builder()
            .title(title.getText().toString())
            .description(body.getText().toString())
            .status(status.getText().toString())
            .build();

    Amplify.API.mutate(
            ModelMutation.create(tasks),
            response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
            error -> Log.e("MyAmplifyApp", "Create failed", error)
    );
submit.setText("submitted");
    total=0;
    Amplify.API.query(
            ModelQuery.list(Task.class, ModelPagination.limit(1_000)),
            response -> {
                for (Task task : response.getData()) {
                   total++;
                    Log.i("MyAmplifyApp", task.toString());
                }
            },
            error -> Log.e("MyAmplifyApp", "Query failure", error)
    );


    totalTask.setText(taskTotal+total);




}

    }

