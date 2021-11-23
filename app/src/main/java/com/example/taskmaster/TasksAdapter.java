package com.example.taskmaster;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.models.Tasks;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    List<Task> allTasks = new ArrayList<Task>();

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public TasksAdapter(List<Task> allTasks) {
        this.allTasks = allTasks;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        private String title;
        public Button mTasks;

        public TasksViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            mTasks = (Button) itemView.findViewById(R.id.titleInFragment);

            itemView.findViewById(R.id.titleInFragment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }

            });
        }

    }


    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_blank, parent, false);
        TasksViewHolder tasksViewHolder = new TasksViewHolder(view, mListener);
        return tasksViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        Task tasks = allTasks.get(position);
        holder.mTasks.setText(tasks.getTitle());


    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }


}