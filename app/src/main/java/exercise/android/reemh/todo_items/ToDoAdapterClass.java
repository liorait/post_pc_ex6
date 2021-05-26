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
/**
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               // if (holder.checkBox.isChecked()) {
                  //   holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    //holder.todoText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
              //  }
               // else {
                  //  holder.todoText.setPaintFlags(0);
               // }

                checkBoxListener.OnCheckBox(item, holder.checkBox.isChecked());
            }
        });
 */

        holder.checkBox.setOnClickListener(v->{
             // UI
            //if (holder.checkBox.isChecked()){
               // holder.todoText.setPaintFlags(holder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //    holder.todoText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
               // holder.checkBox.setChecked(true);
               // holder.checkBox.setChecked(true);
               // holder.editBth.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
          //  }
          //  else {

           //     holder.todoText.setPaintFlags(0);
          //  }


            checkBoxListener.OnCheckBox(item, holder.checkBox.isChecked());
        });


        holder.editDoneBtn.setOnClickListener(v->{
            holder.editBth.setVisibility(View.VISIBLE);
            holder.editDoneBtn.setVisibility(View.GONE);
            holder.todoText.setEnabled(false);
           // holder.todoText.setText(holder.todoText.getText());
            editListener.onEditClick(item, holder.todoText.getText().toString());
        });

        holder.deleteButton.setOnClickListener(v -> {
            //deleteListener.onDeleteClick(position);
            this.list.remove(position);
            //this.list = holder.getCurrentItems();
            //this.addTodoListToAdapter(new ArrayList<>(holder.getCurrentItems()));
            deleteListener.onDeleteClick(item);
        });


        /**
         *
         * holder.deleteButton.setOnClickListener(new View.OnClickListener() {
         *                @Override
         *                public void onClick(View v) {
         *                    //deleteListener.onDeleteClick(position);
         *                    deleteListener.onDeleteClick(item);
         *                }
         *            });
        holder.deleteButton.setOnClickListener(v -> {
            TodoItem item_ = this.list.get(position);


            System.out.println("Going to remove" + item_.desc);
            // this.list.remove(position);

            System.out.println("holders list" + holder.itemsList.toString());
            System.out.println("adapter list" + this.list.toString());

            holder.deleteItem(item_);
            // Create an intent for the activity to know which item to remove from the activity's holder
            //Intent intent = new Intent("Remove item at: ",)


            System.out.println("to print------------------------------------------" + item_.desc);
            //this.list = holder.getCurrentItems();

            this.addTodoListToAdapter(new ArrayList<>(holder.getCurrentItems()));

        });*/

        holder.editBth.setOnClickListener(v -> {
            holder.editBth.setVisibility(View.GONE);
            holder.editDoneBtn.setVisibility(View.VISIBLE);
            holder.todoText.setEnabled(true);
        });



      //  holder.checkBox.setOnClickListener(v->{
       //     if (holder.checkBox.isChecked()){
       //         holder.todoText.setPaintFlags(holder.editBth.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
               // holder.editBth.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
      //      }
     //       else{
                //holder.editBth.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
     //       }
      //  });


    }

    @Override
    // return number of todo_ items holders
    public int getItemCount() {
        return this.list.size();
    }
}
