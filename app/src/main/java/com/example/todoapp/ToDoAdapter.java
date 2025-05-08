package com.example.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ToDoAdapter extends ArrayAdapter<Task> {
    private final ArrayList<Task> tasks;
    private final Context context;

    public ToDoAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        }

        Task task = tasks.get(position);

        CheckBox checkBox = itemView.findViewById(R.id.checkBox);
        TextView taskText = itemView.findViewById(R.id.taskText);
        ImageButton deleteButton = itemView.findViewById(R.id.deleteButton);

        taskText.setText(task.getText());
        checkBox.setOnCheckedChangeListener(null); // clear previous listener
        checkBox.setChecked(task.isDone());

        if (task.isDone()) {
            taskText.setPaintFlags(taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            taskText.setPaintFlags(taskText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
            sortTasks(); // move done to bottom
            notifyDataSetChanged();
        });

        deleteButton.setOnClickListener(v -> {
            tasks.remove(position);
            notifyDataSetChanged();
        });

        return itemView;
    }

    private void sortTasks() {
        Collections.sort(tasks, (t1, t2) -> Boolean.compare(t1.isDone(), t2.isDone()));
    }
}
