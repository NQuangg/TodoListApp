package com.example.todolist.model;

public class Task {
    private String taskName;
    private boolean isChecked;

    public Task(String taskName, boolean isChecked) {
        this.taskName = taskName;
        this.isChecked = isChecked;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void assignTask(Task task) {
        taskName = task.getTaskName();
        isChecked = task.isChecked();
    }
}
