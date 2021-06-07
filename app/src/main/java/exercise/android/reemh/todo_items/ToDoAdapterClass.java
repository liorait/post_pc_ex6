package exercise.android.reemh.todo_items;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ToDoAdapterClass extends RecyclerView.Adapter<TodoItemsHolderImpl>{

    private final ArrayList<TodoItem> list;
    private Context mContext;

    public ToDoAdapterClass(Context context){
        this.list = new ArrayList<>();
        this.mContext = context;
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
    public EditOptionListener editOptionListener;

    public void setEditListener(EditClickListener listener){
        this.editListener = listener;
    }

    public void setDeleteListener(DeleteClickListener listener){
        this.deleteListener = listener;
    }

    public void setCheckBoxListener(CheckBoxListener listener){
        this.checkBoxListener = listener;
    }

    public void setEditOptionMenuListener(EditOptionListener listener){
        this.editOptionListener = listener;
    }

    public interface EditClickListener{
       // void onEditClick(TodoItem item, String description);
        void onEditClick(TodoItem item);
    }

    // Create an interface
    public interface DeleteClickListener{
        void onDeleteClick(TodoItem item);
    }

    public interface CheckBoxListener{
        void OnCheckBox(TodoItem item, boolean isChecked);
    }

    public interface EditOptionListener{
        void onEditOption(int position);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemsHolderImpl holder, int position) {
        TodoItem item = this.list.get(position);
        holder.todoText.setText(item.getDescription());
        holder.todoText.setEnabled(false);
        holder.dateTextView.setText(item.getCreatedDate());

        String status =  this.list.get(position).getStatus();
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
        });

        holder.deleteButton.setOnClickListener(v -> {
            this.list.remove(position);
            deleteListener.onDeleteClick(item);
        });

        holder.editBth.setOnClickListener(v -> {
            editListener.onEditClick(item);
        });

        holder.todoText.setOnClickListener(v ->{
           // System.out.println("clicked");
            editListener.onEditClick(item);
        });

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.buttonViewOption);

                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item_menu) {
                        switch (item_menu.getItemId()) {
                            case R.id.edit_settings:
                                editOptionListener.onEditOption(position);
                                return true;
                                //handle menu1 click
                            case R.id.delete_settings:
                                //handle menu2 click
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    // return number of todo_ items holders
    public int getItemCount() {
        return this.list.size();
    }
}
