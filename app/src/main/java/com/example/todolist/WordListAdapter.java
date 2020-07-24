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
import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private final LinkedList<String> mWordList;
    private final LinkedList<Boolean> stateList;
    private LayoutInflater mInflater;

    public WordListAdapter(Context context, LinkedList<String> wordList, LinkedList<Boolean> stateList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
        this.stateList = stateList;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final WordViewHolder holder, final int position) {
        String mCurrent = mWordList.get(position);
        holder.wordItemView.setText(mCurrent);
        holder.wordItemView.setChecked(stateList.get(position));

        if (holder.wordItemView.isChecked()) {
            holder.wordItemView.setText(Html.fromHtml("<del><span style='color:#808080'>"+mCurrent+"</span></del>", 0));
        } else {
            holder.wordItemView.setText(mCurrent);
        }
        stateList.set(position, holder.wordItemView.isChecked());
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
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
                    String element = mWordList.get(mPosition);
                    if (wordItemView.isChecked()) {
                        wordItemView.setText(Html.fromHtml("<del><span style='color:#808080'>"+element+"</span></del>", 0));
                    } else {
                        wordItemView.setText(element);
                    }
                    stateList.set(mPosition, wordItemView.isChecked());
                }

            });


            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wordItemView.isChecked()) {
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
                            mWordList.set(mPosition, taskName.getText().toString());
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
                        return;
                    }
                    int mPosition = getLayoutPosition();
                    mWordList.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    Toast.makeText(view.getContext(), "delete successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
