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
    //    holder.dateTextView.setText(holder.dateTextView.getText() + " " +  item.getCreatedDate());
        holder.dateTextView.setText(item.getCreatedDate());
       // Context context =


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
         //   holder.editBth.setVisibility(View.VISIBLE);
           // holder.editDoneBtn.setVisibility(View.GONE);
            //holder.todoText.setEnabled(false);

            //editListener.onEditClick(item, holder.todoText.getText().toString());
        });

        holder.deleteButton.setOnClickListener(v -> {
            this.list.remove(position);
            deleteListener.onDeleteClick(item);
        });

        holder.editBth.setOnClickListener(v -> {
           // holder.editBth.setVisibility(View.GONE);
            //holder.editDoneBtn.setVisibility(View.VISIBLE);
           // holder.todoText.setEnabled(true);
            editListener.onEditClick(item); // todo move to on item click
        });

        holder.todoText.setOnClickListener(v ->{
            System.out.println("clicked");
            editListener.onEditClick(item);

            // todo pass intent to second activity with the id or the object
        });

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.buttonViewOption);
               // PopupMenu p = new PopupMenu(holder.buttonViewOption)

                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item_menu) {
                        switch (item_menu.getItemId()) {
                            case R.id.edit_settings:
                               // editListener.onEditClick(item, item.getContentDescription().toString());
                              //  System.out.println("hey");
                              ///  holder.editOption();
                                // todo should open a new activity window
                               // TodoItem itemEdit = holder.getCurrentItems().get(position);
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
