package com.example.taskmaster;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmaster.Repo.TaskDao;
import com.example.taskmaster.models.Tasks;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Tasks> allTasks = new ArrayList<Tasks>();
//    private TaskViewModel taskViewModel;
    TaskDao taskDao;

    private Button addTask;
    private Button allTask;
    private RecyclerView recyclerView;
    private TasksAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createTasksList();
        buildRecyclerView();
        setAllTasksOnClickListener();


//        buildRecyclerView();




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


    public void openTaskDetails(String title,String body, String state) {
        Intent intent = new Intent(this, TaskDetailPage.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        intent.putExtra("state", state);


        startActivity(intent);
    }

    public void createTasksList() {
        AppDatabase appDb = AppDatabase.getInstance(getApplicationContext());
        allTasks.add(new Tasks("task1","steps you will likely want to take to accomplis","new"));
        allTasks.add(new Tasks("task2","refactor your homepage to look snazzy","assigned"));
        allTasks.add(new Tasks("task3","addition to sending the Task title to the detail page","in progress"));
        allTasks.add(new Tasks("task4","ch the detail page with the correct Tas","complete"));
        allTasks.add(new Tasks("task5","opulate your RecyclerView/ViewAdap","assigned"));
        allTasks.add(new Tasks("task6","an tap on any one of the Tasks in the R","new"));
        TaskDao taskDao = appDb.taskDao();
//        allTasks = taskDao.getAllTasks();

//        AppDatabase appDb = AppDatabase.getInstance(getApplicationContext());

//        allTasks = taskDao.getAllTasks();
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhh"+allTasks.size());
    }

//    public void createTasksList() {
//        allTasks.add(new Tasks("task1","steps you will likely want to take to accomplis","new"));
//        allTasks.add(new Tasks("task2","refactor your homepage to look snazzy","assigned"));
//        allTasks.add(new Tasks("task3","addition to sending the Task title to the detail page","in progress"));
//        allTasks.add(new Tasks("task4","ch the detail page with the correct Tas","complete"));
//        allTasks.add(new Tasks("task5","opulate your RecyclerView/ViewAdap","assigned"));
//        allTasks.add(new Tasks("task6","an tap on any one of the Tasks in the R","new"));
//    }

    public void buildRecyclerView() {

        recyclerView = findViewById(R.id.recucler);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TasksAdapter(allTasks);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);


    }

    public void setAllTasksOnClickListener() {
        mAdapter.setOnItemClickListener(new TasksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Tasks task = allTasks.get(position);

                openTaskDetails(task.getTitle(),task.getBody(),task.getState());

            }
        });
    }
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName","the user didn't add a name yet!");
        TextView user = findViewById(R.id.username);
        user.setText(userName);

    }

}