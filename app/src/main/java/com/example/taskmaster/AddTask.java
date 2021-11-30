package com.example.taskmaster;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.Repo.TaskDao;
import com.example.taskmaster.models.Tasks;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

import br.com.onimur.handlepathoz.HandlePathOz;
import br.com.onimur.handlepathoz.HandlePathOzListener;
import br.com.onimur.handlepathoz.model.PathOz;
//HandlePathOzListener.SingleUri

public class AddTask extends AppCompatActivity implements HandlePathOzListener.SingleUri{
private Button addTask;
private EditText title;
private EditText body;
private EditText status;
private TextView submit;
private TextView totalTask;
private Integer total = 0;
private String taskTotal = "Total task: ";
private RadioGroup radioGroup;
private RadioButton radioButton;
private String teamName="Team1";
private Handler handler;
private Team team;
private HandlePathOz handlePathOz;
private Button addFileButton;
    private static final int REQUEST_OPEN_GALLERY = 920;
    private static final int REQUEST_PERMISSION = 123;


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
            radioGroup=(RadioGroup) findViewById(R.id.group);
            addFileButton = (Button) findViewById(R.id.addfile);
            addFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFile();
                }
            });
//            submmit();
//            try {
//                // Add these lines to add the AWSApiPlugin plugins
//                Amplify.addPlugin(new AWSApiPlugin());
//                Amplify.configure(getApplicationContext());
//
//                Log.i("MyAmplifyApp", "Initialized Amplify");
//            } catch (AmplifyException error) {
//                Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
//            }


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
//            total = 0;
//            ArrayList<Task> listTask = new ArrayList<>();
//            Amplify.API.query(
//                    ModelQuery.list(Task.class, ModelPagination.limit(1_000)),
//                    response -> {
//                        for (Task task : response.getData()) {
//                            total++;
//                            listTask.add(task);
//                            Log.i("MyAmplifyApp", task.toString());
//                        }
//
//                    },
//                    error -> Log.e("MyAmplifyApp", "Query failure", error)
//            );
//            totalTask.setText(taskTotal+total);

        }

    public void checkButton(View v){

        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioButtonId);
        teamName= radioButton.getText().toString();

    }
@SuppressLint("IntentReset")
    public void openFile(){
        Intent intent;
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra("return-data", true);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, REQUEST_OPEN_GALLERY);
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                //set Uri to handle
                handlePathOz.getRealPath(uri);
                //show Progress Loading
            }
        }
    }
public void process(){
//    AppDatabase appDb = AppDatabase.getInstance(this.getApplicationContext());
//
//    TaskDao taskDao = appDb.taskDao();
//taskDao.insertAll(new Tasks(title.getText().toString(),body.getText().toString(),status.getText().toString()));
    handler = new Handler(Looper.getMainLooper(),
            message -> {
                String url = getSharedPreferences("myPref", MODE_PRIVATE).getString("fileUrl", "No File");
                    Task task1 = com.amplifyframework.datastore.generated.model.Task.builder()
                            .title(title.getText().toString())
                            .description(body.getText().toString())
                            .status(status.getText().toString())
                            .team(this.team)
                            .fileUrl(url)
                            .build();
                    Amplify.API.mutate(
                            ModelMutation.create(task1),
                            saved -> Log.i("teamUpdate", "Team Update"),
                            failure -> Log.e("teamUpdate", "STeam failure", failure)
                    );
                    Log.i("teamname", this.team.getName());

                return false;
            });

    Amplify.API.query(
            ModelQuery.list(Team.class, Team.NAME.contains(this.teamName)),
            response -> {
                for (Team team : response.getData()) {
                    this.team=team;
                    Log.i("MyAmplifyApp", team.getName());
                }
                handler.sendEmptyMessage(1);
            },
            error -> Log.e("MyAmplifyApp", "Query failure", error)
    );
//    Task tasks = Task.builder()
//            .title(title.getText().toString())
//            .description(body.getText().toString())
//            .status(status.getText().toString())
//            .build();
//
//    Amplify.API.mutate(
//            ModelMutation.create(tasks),
//            response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//            error -> Log.e("MyAmplifyApp", "Create failed", error)
//    );


submit.setText("submitted");
    total=0;
//    Amplify.API.query(
//            ModelQuery.list(Task.class, ModelPagination.limit(1_000)),
//            response -> {
//                for (Task task : response.getData()) {
//                   total++;
//                    Log.i("MyAmplifyApp", task.toString());
//                }
//
//            },
//            error -> Log.e("MyAmplifyApp", "Query failure", error)
//    );
    totalTask.setText(taskTotal+total);

}


    @Override
    public void onRequestHandlePathOz(@NonNull PathOz pathOz, @Nullable Throwable throwable) {
        String fileName = FilenameUtils.getName(pathOz.getPath());
        File file = new File(pathOz.getPath());
        Amplify.Storage.uploadFile(
                fileName,
                file,
                uploadFileResult -> {
                    Log.i("MyAmplifyApp", "Successfully uploaded: " + uploadFileResult.getKey());
                    Amplify.Storage.getUrl(
                            uploadFileResult.getKey(),
                            result -> {
                                Log.i("MyAmplifyApp", "Successfully generated: " + result.getUrl());
                            },
                            error -> Log.e("MyAmplifyApp", "URL generation failure", error)
                    );
                },
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

//public void onResume(){
//    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddTask.this);
//    String userName = sharedPreferences.getString("userName", "the user didn't add a name yet!");
//    TextView user = findViewById(R.id.username);
//    user.setText(userName);
//    this.teamName = "Team1";}
//public void shareUserName() {
//
//
//}
}



