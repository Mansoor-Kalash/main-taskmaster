package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsPage extends AppCompatActivity {
private Button save;
private RadioGroup radioGroup;
private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        save = (Button) findViewById(R.id.save);
        radioGroup = (RadioGroup) findViewById(R.id.groups);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUserName();
            }
        });


    }
    public void checkButton(View v){
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioButtonId);
        shareUserName();
    }
    public void shareUserName(){
//        String user = username.getText().toString();
        String teamName = radioButton.getText().toString();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsPage.this);
        sharedPreferences.edit().putString("teamName",teamName).apply();
        Toast.makeText(SettingsPage.this,"submitted!", Toast.LENGTH_LONG).show();
    }
}