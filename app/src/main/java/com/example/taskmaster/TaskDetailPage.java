package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetailPage extends AppCompatActivity {
private TextView title;
private TextView body;
private TextView state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_page);
        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);
        state = (TextView) findViewById(R.id.state);
//        Intent intent =
        String title =  getIntent().getExtras().get("title").toString();
        String body =  getIntent().getExtras().get("body").toString();
        String state =  getIntent().getExtras().get("state").toString();

        putTitle(title,body,state);

    }
    public void putTitle(String title,String body, String state){
        this.title.setText(title);
        this.body.setText(body);
        this.state.setText(state);
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TaskDetailPage.this);
        String userName = sharedPreferences.getString("userName","the user didn't add a name yet!");
        Toast.makeText(this, userName, Toast.LENGTH_LONG).show();

    }
}