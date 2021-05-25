package exercise.android.reemh.todo_items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// TODO: implement!
public class TodoItemsHolderImpl extends RecyclerView.ViewHolder implements TodoItemsHolder{
  // a class that shows one view

  protected ArrayList<TodoItem> itemsList; // list of todo_ items
  private String IN_PROGRESS = "IN-PROGRESS";
  private String DONE = "DONE";
  private int index = 0;
  EditText todoText;
  TextView dateTextView;
  CheckBox checkBox;
  Button editBth;
  Button editDoneBtn;
  Button deleteButton;




 // private View view = null;

  public TodoItemsHolderImpl(View view){
    super(view);
    this.itemsList = new ArrayList<>();
   // view = view;
    // find view by id text view
    todoText = view.findViewById(R.id.toDoTextView);
    checkBox = view.findViewById(R.id.checkBox);
    dateTextView = view.findViewById(R.id.createdDateTextView);
    editBth = view.findViewById(R.id.editButton);
    editDoneBtn = view.findViewById(R.id.editDoneBtn);
    deleteButton = view.findViewById(R.id.deleteButton);
  }

  @Override
  public ArrayList<TodoItem> getCurrentItems() { return this.itemsList; }

  /**
   * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
   * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
   */
  @Override
  public void addNewInProgressItem(String description) {
    TodoItem newItem = new TodoItem(description, IN_PROGRESS);
    this.itemsList.add(0, newItem);
    System.out.println("list after add" + this.itemsList.toString());
  }

  @Override
  public void markItemDone(TodoItem item) {
    if (itemsList.contains(item)) {
      int index = itemsList.indexOf(item);
      itemsList.get(index).setStatus(DONE);
    }
  }

  @Override
  public void markItemInProgress(TodoItem item) {
    if (itemsList.contains(item)) {
      int index = itemsList.indexOf(item);
      itemsList.get(index).setStatus(IN_PROGRESS);
    }
  }

  @Override
  public void deleteItem(TodoItem item) {
    System.out.println("holder print" + item.desc);
    System.out.println("list before delete" + this.itemsList.toString());
    if (itemsList.contains(item)) {
      System.out.println("entered if");
      this.itemsList.remove(item);
      System.out.println("list after delete" + this.itemsList.toString());
    }
  }
}
