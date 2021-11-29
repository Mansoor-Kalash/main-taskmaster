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

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.UserStateListener;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.GraphQLOperation;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.Repo.TaskDao;
import com.example.taskmaster.models.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
public  Handler handler= new Handler();
//private Handler handlerCreateTask;
    private List<Task> allTasks = new ArrayList<Task>();
    private List<Team> allTeams = new ArrayList<Team>();
    private String teamName;
    private Button signOut;
    private int isLogged;
    private static final String TAG = "fisher.MainActivity";
//    private TaskViewModel taskViewModel;
//    TaskDao taskDao;
    AWSMobileClient awsMobileClient;
    private Button addTask;
    private Button allTask;
    private RecyclerView recyclerView;
    private TasksAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Team team;
    private TextView userNameLog;
    private String usernameLog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameLog = (TextView) findViewById(R.id.userName);

            try {
                // Add these lines to add the AWSApiPlugin plugins
                Amplify.addPlugin(new AWSApiPlugin());
                Amplify.addPlugin(new AWSCognitoAuthPlugin());
                Amplify.configure(getApplicationContext());

                Log.i("MyAmplifyApp", "Initialized Amplify");
            } catch (AmplifyException error) {
                Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
            }
        signOut = (Button) findViewById(R.id.signout);

        if (AWSMobileClient.getInstance().isSignedIn()){
//            signOut();
            signOut.setText("Sign Out");


            userNameLog.setText("username: "+AWSMobileClient.getInstance().getUsername().toString());
            isLogged=0;
        }else if(!AWSMobileClient.getInstance().isSignedIn()){
//            signIn();
            signOut.setText("Sign In");

            userNameLog.setText("username:");
            isLogged=1;
        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isLogged == 0){
//                    signIn();
//                    signOut.setText("Sign Out");
//                    isLogged = 1;
//
//                }else {
//                    signOut();
//                    signOut.setText("Sign In");
//                    isLogged = 0;
//                }
                if (AWSMobileClient.getInstance().isSignedIn()){
                    signOut();
//                    isLogged=0;
//                    signOut.setText("Sign Out");
//                    userNameLog.setText("username: ");


                }else if(!AWSMobileClient.getInstance().isSignedIn()){
                    signIn();
//                    signOut.setText("Sign In");
//                    userNameLog.setText("username: "+ usernameLog);
//
//                    isLogged=1;
                }

            }
        });



        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );
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

        Log.i("track","after Handler");
        Amplify.API.query(
                ModelQuery.list(Team.class, Team.NAME.contains(teamName)),
                response -> {
                    for (Team team : response.getData()) {
                        this.team=team;
                        Log.i("MyAmplifyApp", team.getName());

                    }
                    allTasks=this.team.getTask();
                    handler.sendEmptyMessage(1);
                    Log.i("track","after message");
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

    //----------------------------Auth---------------------------------//

    public void signIn(){

        Amplify.Auth.signInWithWebUI(
                this,
                result -> Log.i("AuthQuickStart", result.toString()),
                error -> Log.e("AuthQuickStart", error.toString())
        );
//        onResume();
        handler.sendEmptyMessage(2);
    }
    public void signOut(){
        Amplify.Auth.signOut(
                () -> Log.i("AuthQuickstart", "Signed out successfully"),
                error -> Log.e("AuthQuickstart", error.toString()));
        handler.sendEmptyMessage(2);
    }

    public void setIsLogged(){

        if (AWSMobileClient.getInstance().isSignedIn()) {
             usernameLog = AWSMobileClient.getInstance().getUsername();
            Log.i(TAG, "fisher.signin" + usernameLog);
            signOut.setText("Sign Out");
            isLogged=1;
handler.sendEmptyMessage(2);



        }else if(!AWSMobileClient.getInstance().isSignedIn()){
            userNameLog.setText("username: ");
            signOut.setText("Sign In");
            isLogged=0;

        }
    }
    protected  void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName", "the user didn't add a name yet!");
        TextView user = findViewById(R.id.username);
        user.setText(userName);
        this.teamName = userName;
        handler = new Handler(Looper.getMainLooper(),
                new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message message) {
                        if (message.what==1){
                            recyclerView.getAdapter().notifyDataSetChanged();
                            Log.i("inside handler",allTasks.toString());
                        }else if (message.what==2){


                            userNameLog.setText("username: "+ usernameLog);

                            Log.i("userNameHandler",userNameLog.getText().toString());
                        }

                        return false;
                    }
                });
        setIsLogged();

        createTasksList();
        Log.i("track","after Create Liste");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isLogged==0){
            userNameLog.setText("username: ");
        }else if(isLogged == 1){
            userNameLog.setText("username: "+usernameLog);
        }

        buildRecycl();
        Log.i("track","after build");

        setAllTasksOnClickListener();



    }


}