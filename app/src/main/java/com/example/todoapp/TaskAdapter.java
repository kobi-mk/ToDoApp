package com.example.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final ArrayList<Task> tasks;
    private final Context context;

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView taskText;
        ImageButton deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            taskText = itemView.findViewById(R.id.taskText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.checkBox.setOnCheckedChangeListener(null); // clear previous listener

        holder.taskText.setText(task.getText());
        holder.checkBox.setChecked(task.isDone());

        if (task.isDone()) {
            holder.taskText.setPaintFlags(holder.taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.taskText.setPaintFlags(holder.taskText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);

            // ✅ Delay sort and notify to prevent crash
            new Handler(Looper.getMainLooper()).post(() -> {
                sortTasks();
                notifyDataSetChanged();
            });
        });

        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                tasks.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < tasks.size() && toPosition < tasks.size()) {
            Task movedTask = tasks.remove(fromPosition);
            tasks.add(toPosition, movedTask);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    private void sortTasks() {
        tasks.sort((a, b) -> Boolean.compare(a.isDone(), b.isDone()));
    }
}
