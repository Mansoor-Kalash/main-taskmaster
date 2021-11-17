//package com.example.taskmaster;
//
//
//
//import android.app.Application;
//
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//
//import com.example.taskmaster.Repo.TaskRepo;
//import com.example.taskmaster.models.Tasks;
//
//import java.util.List;
//
//public class TaskViewModel extends AndroidViewModel {
//    private TaskRepo taskRepo;
//    private List<Tasks> allTask;
//
//    public TaskViewModel(@NonNull Application application) {
//        super(application);
//        taskRepo = new TaskRepo(application);
//        allTask = taskRepo.getAllTasks();
//    }
//
//    public void insert(Tasks tasks) {
//        taskRepo.insert(tasks);
//    }
//    public List<Tasks> getAllTask() {
//        return allTask;
//    }
//
//
//}
