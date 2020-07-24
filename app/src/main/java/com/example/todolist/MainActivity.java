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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
            readJSONDataFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        try {
            writeJSONDataFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJSONDataFromFile() throws IOException{
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < taskList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("taskName", taskList.get(i).getTaskName());
            jsonObject.put("isChecked", taskList.get(i).isChecked());
            jsonArray.add(jsonObject);
        }

        FileOutputStream fos = null;
        try {
            fos = openFileOutput("taskList.json", MODE_PRIVATE);
            String str = jsonArray.toJSONString();
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJSONDataFromFile() throws IOException{
        FileInputStream fis = null;
        try {
            fis = openFileInput("taskList.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuffer data = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
            JSONParser parser = new JSONParser();
            try {
                JSONArray jsonArray = (JSONArray) parser.parse(String.valueOf(data));

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String taskName = (String) jsonObject.get("taskName");
                    Boolean check = (Boolean) jsonObject.get("isChecked");
                    taskList.add(new Task(taskName, check));
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}