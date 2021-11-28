package com.example.taskmaster;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.Repo.TaskDao;
import com.example.taskmaster.models.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
public  Handler handler;
//private Handler handlerCreateTask;
    private List<Task> allTasks = new ArrayList<Task>();
    private List<Team> allTeams = new ArrayList<Team>();
    private String teamName;
//    private TaskViewModel taskViewModel;
//    TaskDao taskDao;

    private Button addTask;
    private Button allTask;
    private RecyclerView recyclerView;
    private TasksAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Team team;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




            try {
                // Add these lines to add the AWSApiPlugin plugins
                Amplify.addPlugin(new AWSApiPlugin());
                Amplify.configure(getApplicationContext());

                Log.i("MyAmplifyApp", "Initialized Amplify");
            } catch (AmplifyException error) {
                Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
            }


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






    public  void setAllTasksOnClickListener() {
        mAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Task task = allTasks.get(position);

                openTaskDetails(task.getTitle(), task.getDescription(), task.getStatus());

            }
        });
    }
    public void createTasksList() {
        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                        Log.i("inside handler",allTask.toString());
                        return false;
                    }
                });
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains(teamName)),
                response -> {
                    for (Team team : response.getData()) {
                        this.team=team;
                        Log.i("MyAmplifyApp", team.getName());

                    }
                    allTasks=this.team.getTask();

                    handler.sendEmptyMessage(1);



                    Log.i("MyAmplifyApp", allTasks.toString());

                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );




    }
    public void buildRecycl (){
        recyclerView = findViewById(R.id.recucler);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mAdapter = new TasksAdapter(allTasks);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }
    protected  void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName", "the user didn't add a name yet!");
        TextView user = findViewById(R.id.username);
        user.setText(userName);
        this.teamName = userName;
        createTasksList();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buildRecycl();
        setAllTasksOnClickListener();



    }


}