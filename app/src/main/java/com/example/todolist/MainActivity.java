package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<Task> taskList = new ArrayList<Task>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int taskListSize = taskList.size();
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_add);
                Button submitButton = dialog.findViewById(R.id.submit_button);
                Button cancelButton = dialog.findViewById(R.id.cancel_button);

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText taskName = dialog.findViewById(R.id.task_name);
                        String task = taskName.getText().toString().trim().replaceAll("\\s+", " ");
                        if (task.equals("")) {
                            Toast.makeText(view.getContext(), "Task is empty", Toast.LENGTH_SHORT).show();
                        } else {
                            taskList.add(new Task(task, false));
                            mRecyclerView.getAdapter().notifyItemInserted(taskListSize);
                            mRecyclerView.smoothScrollToPosition(taskListSize);
                            dialog.dismiss();
                            Toast.makeText(view.getContext(), "Add successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new WordListAdapter(this, taskList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            readInternalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        try {
            writeInternalFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeInternalFile() throws IOException{
        String fileContents = new Gson().toJson(taskList);

        FileOutputStream fos = null;
        try {
            fos = openFileOutput("taskList.json", MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInternalFile() throws IOException{
        FileInputStream fis = null;
        try {
            fis = openFileInput("taskList.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuffer data = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
            fis.close();

            String fileContents = data.toString();
            ArrayList<Task> tasks =  new Gson().fromJson(fileContents.trim(), new TypeToken<ArrayList<Task>>(){}.getType());

            for (Task task: tasks) {
                taskList.add(task);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}