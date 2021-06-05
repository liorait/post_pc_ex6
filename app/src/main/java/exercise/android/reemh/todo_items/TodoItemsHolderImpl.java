package exercise.android.reemh.todo_items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;


public class TodoItemsHolderImpl extends RecyclerView.ViewHolder implements TodoItemsHolder{
  // a class that shows one view

  protected ArrayList<TodoItem> itemsList; // list of todo_ items
  private String IN_PROGRESS = "IN-PROGRESS";
  private String DONE = "DONE";
  EditText todoText;
  TextView dateTextView;
  CheckBox checkBox;
  Button editBth;
  Button editDoneBtn;
  TextView deleteButton;
  //Button buttonViewOption;
  TextView buttonViewOption;


  public TodoItemsHolderImpl(View view){
    super(view);
    this.itemsList = new ArrayList<>();

    // find view by id text view
    todoText = view.findViewById(R.id.toDoTextView);
    checkBox = view.findViewById(R.id.checkBox);
    dateTextView = view.findViewById(R.id.createdDateTextView);
    editBth = view.findViewById(R.id.editButton);
    editDoneBtn = view.findViewById(R.id.editDoneBtn);
    deleteButton = view.findViewById(R.id.deleteButton);
    buttonViewOption = view.findViewById(R.id.buttonViewOption);
  }

  @Override
  public ArrayList<TodoItem> getCurrentItems() {
    return this.itemsList; }

  /**
   * Creates a new TodoItem and adds it to the list, with the @param description and status=IN-PROGRESS
   * Subsequent calls to [getCurrentItems()] should have this new TodoItem in the list
   */
  @Override
  public void addNewInProgressItem(String description) {
   // TodoItem newItem = new TodoItem(  description, IN_PROGRESS);
    //this.itemsList.add(0, newItem);
  }

  @Override
  public void markItemDone(TodoItem item) {
      /**
    if (itemsList.contains(item)) {
      int index = itemsList.indexOf(item);
      itemsList.get(index).setStatus(DONE);

      // move to the bottom of the list
      TodoItem temp = new TodoItem(item.getDescription(), item.getStatus());
      Date date = item.getCreatedDateAsDate();
      this.itemsList.remove(item);
      this.itemsList.add(this.itemsList.size(), temp);
      temp.setCreatedDate(date);
    }*/
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  public void markItemInProgress(TodoItem item) {
      /**
    if (itemsList.contains(item)) {
      int index = itemsList.indexOf(item);
      itemsList.get(index).setStatus(IN_PROGRESS);

      // move to the start
      TodoItem temp = new TodoItem(item.getDescription(), item.getStatus());
      Date date = item.getCreatedDateAsDate();

      this.itemsList.remove(item);
      this.itemsList.add(0, temp);
      temp.setCreatedDate(date);
      this.itemsList.sort(new Comparator<TodoItem>() {
          @Override
          public int compare(TodoItem o1, TodoItem o2) {
              if (o1.getStatus().equals(IN_PROGRESS) && o2.getStatus().equals(IN_PROGRESS)) {
                  return o2.getCreatedDate().compareTo(o1.getCreatedDate());
              }

              return 0;
          }
      });
    }*/
  }

  @Override
  public void editItem(TodoItem item, String description){
      /**
    if (itemsList.contains(item)) {
      int index = itemsList.indexOf(item);
      itemsList.get(index).setDescription(description);
    }*/
  }

  @Override
  public void deleteItem(TodoItem item) {
      /**
    if (itemsList.contains(item)) {
      this.itemsList.remove(item);
    }*/
  }

  private ArrayList<String> saveItemsRepresentation(){
      // goes over the list items and for each item create string representation and save
      ArrayList<String> itemsStrList = new ArrayList<>();

      for (int i = 0; i < this.itemsList.size(); i++){
          String itemStr = this.itemsList.get(i).itemStringRepresentation();
          itemsStrList.add(itemStr);
      }

      return itemsStrList;
  }

  @Override
  public Serializable saveState() {
      // create new holderState
      ToDoItemsHolderImplState holder_state = new ToDoItemsHolderImplState();

      // get items representation array list
      ArrayList<String> itemsStrList = saveItemsRepresentation();

      // save the list into the state
      holder_state.itemsRepresentationList = itemsStrList;

      return holder_state;
  }

  private void convertStringListToTodoList(ArrayList<String> listStr) throws ParseException {
      for (int i = 0; i < listStr.size(); i++)
      {
          /**
          String val = listStr.get(i);
          String[] split = val.split("/");
          String desc = split[0];
          String status = split[1];
          String date = split[2];*/

          // create a new item
          TodoItem newItem = TodoItem.stringToToDoItem(listStr.get(i));
         // System.out.println("string format: " + date);
        // Date convertedDate = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss").parse(date);
          //long convertedDate = Date.parse(date);
          SimpleDateFormat convertedDate = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
          Date date1 = convertedDate.parse(newItem.getCreatedDate());

          newItem.setCreatedDate(date1);
          this.itemsList.add(newItem);
      }
  }

  @Override
  public void loadState(Serializable prevState) throws ParseException {
     if (!(prevState instanceof ToDoItemsHolderImplState)) {
         return; // ignore
     }
     ToDoItemsHolderImplState casted = (ToDoItemsHolderImplState)prevState;
     // convert the represented items list to an ItemTodo list
     convertStringListToTodoList(casted.itemsRepresentationList);
  }

  private static class ToDoItemsHolderImplState implements Serializable {
      private ArrayList<String> itemsRepresentationList;
      private String newTaskTest;
  }
}
