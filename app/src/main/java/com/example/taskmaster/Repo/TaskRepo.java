//package com.example.taskmaster.Repo;
//
//import android.app.Application;
//import android.os.AsyncTask;
//
//import androidx.lifecycle.LiveData;
//
//import com.example.taskmaster.AppDatabase;
//import com.example.taskmaster.models.Tasks;
//
//import java.util.List;
//
//public class TaskRepo {
//    private TaskDao taskDao;
//    private List<Tasks> allTasks;
//
//    public TaskRepo(Application application) {
//        AppDatabase database = AppDatabase.getInstance(application);
//        taskDao = database.taskDao();
//        allTasks = taskDao.getAllTasks();
//    }
//    public void insert(Tasks tasks) {
//        new InsertNoteAsyncTask(taskDao).execute(tasks);
//    }
//    public List<Tasks> getAllTasks() {
//        return allTasks;
//    }
//    private static class InsertNoteAsyncTask extends AsyncTask<Tasks, Void, Void> {
//        private TaskDao taskDao;
//
//        private InsertNoteAsyncTask(TaskDao taskDao) {
//            this.taskDao = taskDao;
//        }
//
//        @Override
//        protected Void doInBackground(Tasks... notes) {
//            taskDao.insert(notes[0]);
//            return null;
//        }
//
//    }}
