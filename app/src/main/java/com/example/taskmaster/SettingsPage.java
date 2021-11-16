package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsPage extends AppCompatActivity {
private EditText  username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        username = (EditText) findViewById(R.id.username);
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUserName();
            }
        });

    }
    public void shareUserName(){
        String user = username.getText().toString();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsPage.this);
        sharedPreferences.edit().putString("userName",user).apply();
        Toast.makeText(SettingsPage.this,"submitted!", Toast.LENGTH_LONG).show();
    }
}