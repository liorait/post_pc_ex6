package exercise.android.reemh.todo_items;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ToDoAdapterClass extends RecyclerView.Adapter<TodoItemsHolderImpl>{

    private final ArrayList<TodoItem> list;

    public ToDoAdapterClass(){
        this.list = new ArrayList<>();
    }

    public void addTodoListToAdapter(ArrayList<TodoItem> newList){
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoItemsHolderImpl onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_todo_item, parent,
                false);
        return new TodoItemsHolderImpl(view);
    }

    public DeleteClickListener deleteListener;
    public EditClickListener editListener;
    public CheckBoxListener checkBoxListener;

    public void setEditListener(EditClickListener listener){
        this.editListener = listener;
    }

    public void setDeleteListener(DeleteClickListener listener){
        this.deleteListener = listener;
    }

    public void setCheckBoxListener(CheckBoxListener listener){
        this.checkBoxListener = listener;
    }

    public interface EditClickListener{
        void onEditClick(TodoItem item, String description);
    }

    // Create an interface
    public interface DeleteClickListener{
        void onDeleteClick(TodoItem item);
    }

    public interface CheckBoxListener{
        void OnCheckBox(TodoItem item, boolean isChecked);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemsHolderImpl holder, int position) {
        TodoItem item = this.list.get(position);
        holder.todoText.setText(item.desc);
        holder.todoText.setEnabled(false);
        holder.dateTextView.setText(item.getCreatedDate());

        String status =  this.list.get(position).status;
        if (status.equals("DONE")){
            holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setChecked(true);
        }
        else{
            holder.checkBox.setChecked(false);
            holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() & Paint.LINEAR_TEXT_FLAG);
        }

        holder.checkBox.setOnClickListener(v->{
            checkBoxListener.OnCheckBox(item, holder.checkBox.isChecked());
        });


        holder.editDoneBtn.setOnClickListener(v->{
            holder.editBth.setVisibility(View.VISIBLE);
            holder.editDoneBtn.setVisibility(View.GONE);
            holder.todoText.setEnabled(false);
            editListener.onEditClick(item, holder.todoText.getText().toString());
        });

        holder.deleteButton.setOnClickListener(v -> {
            this.list.remove(position);
            deleteListener.onDeleteClick(item);
        });

        holder.editBth.setOnClickListener(v -> {
            holder.editBth.setVisibility(View.GONE);
            holder.editDoneBtn.setVisibility(View.VISIBLE);
            holder.todoText.setEnabled(true);
        });
    }

    @Override
    // return number of todo_ items holders
    public int getItemCount() {
        return this.list.size();
    }
}
