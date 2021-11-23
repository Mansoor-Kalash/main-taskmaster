package com.example.taskmaster;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.GraphQLOperation;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.Repo.TaskDao;
import com.example.taskmaster.models.Tasks;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Task> allTasks = new ArrayList<Task>();
//    private TaskViewModel taskViewModel;
//    TaskDao taskDao;

    private Button addTask;
    private Button allTask;
    private RecyclerView recyclerView;
    private TasksAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        synchronized (this) {
            try {
                // Add these lines to add the AWSApiPlugin plugins
                Amplify.addPlugin(new AWSApiPlugin());
                Amplify.configure(getApplicationContext());

                Log.i("MyAmplifyApp", "Initialized Amplify");
            } catch (AmplifyException error) {
                Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
            }
        }


        createTasksList();


        setAllTasksOnClickListener();

        addTask = (Button) findViewById(R.id.addTask);
        allTask = (Button) findViewById(R.id.allTask);


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


    }

    public void addTaskActivity() {
        Intent intent = new Intent(this, AddTask.class);
        startActivity(intent);
    }

    public void allTaskActivity() {
        Intent intent = new Intent(this, AllTask.class);
        startActivity(intent);
    }


    public void openSettingPage() {
        Intent intent = new Intent(this, SettingsPage.class);
        startActivity(intent);
    }


    public void openTaskDetails(String title, String body, String state) {
        Intent intent = new Intent(this, TaskDetailPage.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("state", state);


        startActivity(intent);
    }

    public void createTasksList() {

//        AppDatabase appDb = AppDatabase.getInstance(this.getApplicationContext());
//        allTasks.add(new Tasks("task1","steps you will likely want to take to accomplis","new"));
//        allTasks.add(new Tasks("task2","refactor your homepage to look snazzy","assigned"));
//        allTasks.add(new Tasks("task3","addition to sending the Task title to the detail page","in progress"));
//        allTasks.add(new Tasks("task4","ch the detail page with the correct Tas","complete"));
//        allTasks.add(new Tasks("task5","opulate your RecyclerView/ViewAdap","assigned"));
//        allTasks.add(new Tasks("task6","an tap on any one of the Tasks in the R","new"));
//        TaskDao taskDao = appDb.taskDao();
//        taskDao.insertAll(new Tasks("task2","refactor your homepage to look snazzy","assigned"));

        allTasks = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(Task.class, ModelPagination.limit(1_000)),

                response -> {
                    for (Task task : response.getData()) {
                        allTasks.add(task);
                    }
                    Log.i("MyAmplifyApp", allTasks.toString());

                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerView = findViewById(R.id.recucler);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TasksAdapter(allTasks);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);


//        allTasks = taskDao.getAllTasks();


    }

//    public synchronized void rr() {
////        try {
////            Thread.sleep(100);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//
//        recyclerView = findViewById(R.id.recucler);
//        recyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new TasksAdapter(allTasks);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(mAdapter);
//
//
//    }




    public synchronized void setAllTasksOnClickListener() {
        mAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Task task = allTasks.get(position);

                openTaskDetails(task.getTitle(), task.getDescription(), task.getStatus());

            }
        });
    }

    protected synchronized void onResume() {
        super.onResume();
        createTasksList();
        setAllTasksOnClickListener();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName", "the user didn't add a name yet!");
        TextView user = findViewById(R.id.username);
        user.setText(userName);

    }

}