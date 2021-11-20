package com.example.taskmaster;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.taskmaster.Repo.TaskDao;
import com.example.taskmaster.models.Tasks;

@Database(version = 1, entities = {Tasks.class},exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

public abstract TaskDao taskDao();

    public static AppDatabase  instance;


    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"tasks").allowMainThreadQueries().build();
        }
        return instance;
    }

    }

