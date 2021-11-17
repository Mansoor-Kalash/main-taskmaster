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

@Database(version = 1, entities = {Tasks.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "task_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private PopulateDbAsyncTask(AppDatabase db) {
            taskDao = db.taskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            taskDao.insert(new Tasks("task1","steps you will likely want to take to accomplis","new"));
            taskDao.insert(new Tasks("task2","refactor your homepage to look snazzy","assigned"));
            taskDao.insert(new Tasks("task3","addition to sending the Task title to the detail page","in progress"));
            taskDao.insert(new Tasks("task4","ch the detail page with the correct Tas","complete"));
            taskDao.insert(new Tasks("task5","opulate your RecyclerView/ViewAdap","assigned"));
            taskDao.insert(new Tasks("task6","an tap on any one of the Tasks in the R","new"));
            return null;

        }

//    Tasks taskDao = db.
//    @NonNull
//    @Override
//    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
//        return null;
//    }
//
//    @NonNull
//    @Override
//    protected InvalidationTracker createInvalidationTracker() {
//        return null;
//    }
//
//    @Override
//    public void clearAllTables() {
//
//    }
//
//    public static AppDatabase getDb() {
//        return db;
//    }
//
//    public static void setDb(AppDatabase db) {
//        AppDatabase.db = db;
//    }
    }
}
