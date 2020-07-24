package com.example.todolist;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.model.Task;

import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private final LinkedList<Task> taskList;
    private LayoutInflater mInflater;

    public WordListAdapter(Context context, LinkedList<Task> taskList) {
        mInflater = LayoutInflater.from(context);
        this.taskList = taskList;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final WordViewHolder holder, final int position) {
        String mText = taskList.get(position).getTaskName();
        Boolean mCheck = taskList.get(position).isChecked();
        holder.wordItemView.setText(mText);
        holder.wordItemView.setChecked(mCheck);

        if (holder.wordItemView.isChecked()) {
            holder.wordItemView.setText(Html.fromHtml("<del><span style='color:#808080'>"+mText+"</span></del>", 0));
        } else {
            holder.wordItemView.setText(mText);
        }
        taskList.set(position, new Task(mText, mCheck));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }



    public class WordViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox wordItemView;
        public final ImageView editButton;
        public final ImageView deleteButton;
        final WordListAdapter mAdapter;

        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;

            wordItemView = itemView.findViewById(R.id.word);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            wordItemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    int mPosition = getLayoutPosition();
                    String element = taskList.get(mPosition).getTaskName();
                    if (wordItemView.isChecked()) {
                        wordItemView.setText(Html.fromHtml("<del><span style='color:#808080'>"+element+"</span></del>", 0));
                    } else {
                        wordItemView.setText(element);
                    }
                    taskList.set(mPosition, new Task(element, wordItemView.isChecked()));
                }

            });


            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wordItemView.isChecked()) {
                        Toast.makeText(view.getContext(), "Can't edit task completed", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    final Dialog dialog = new Dialog(view.getContext());
                    dialog.setContentView(R.layout.dialog_add);
                    Button submitButton = dialog.findViewById(R.id.submit_button);
                    Button cancelButton = dialog.findViewById(R.id.cancel_button);

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int mPosition = getLayoutPosition();
                            EditText taskName = dialog.findViewById(R.id.task_name);
                            wordItemView.setText(taskName.getText().toString());
                            taskList.set(mPosition, new Task(taskName.getText().toString(), wordItemView.isChecked()));
                            dialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    mAdapter.notifyDataSetChanged();
                    dialog.show();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wordItemView.isChecked()) {
                        Toast.makeText(view.getContext(), "Can't delete task completed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int mPosition = getLayoutPosition();
                    taskList.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    Toast.makeText(view.getContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
