package com.example.todoapp;

import android.content.Context;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> todoList;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.todoRecyclerView);
        Button addButton = findViewById(R.id.addButton);

        todoList = new ArrayList<>();
        adapter = new TaskAdapter(this, todoList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ✅ Enable drag-and-drop
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView rv,
                                  @NonNull RecyclerView.ViewHolder from,
                                  @NonNull RecyclerView.ViewHolder to) {
                int fromPos = from.getAdapterPosition();
                int toPos = to.getAdapterPosition();
                adapter.moveItem(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // No swipe action
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        addButton.setOnClickListener(v -> showAddDialog(adapter));
    }

    private void showAddDialog(TaskAdapter adapter) {
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setHint("Enter your task");

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(40, 0, 40, 0);
        input.setLayoutParams(params);
        container.addView(input);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add New Task")
                .setView(container)
                .setPositiveButton("Add", (d, which) -> {
                    String taskText = input.getText().toString().trim();
                    if (!taskText.isEmpty()) {
                        Task task = new Task(taskText);
                        todoList.add(0, task); // ✅ Add at top
                        adapter.notifyItemInserted(0);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            input.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        dialog.show();
    }
}
