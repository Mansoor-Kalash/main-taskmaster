package com.example.taskmaster.Repo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.taskmaster.models.Tasks;

import java.util.List;

@Dao
public interface TaskDao {


        @Insert
        void insertAll(Tasks... tasks);
//@Insert
//void insert(Tasks tasks);

        @Query("SELECT * FROM tasks")
    List<Tasks> getAllTasks();

    }
