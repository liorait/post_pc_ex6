package exercise.android.reemh.todo_items;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public TodoItemsHolder holder = null;
  public ToDoAdapterClass adapter = null;
  public LocalDataBase dataBase = null;

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    RecyclerView recyclerView = findViewById(R.id.recyclerTodoItemsList); // finds the recycler view
    adapter = new ToDoAdapterClass(this);

  // todo write this code from each activity
    if (dataBase == null){
      dataBase = ToDoItemsApplication.getInstance().getDataBase();
      dataBase.getCopies();
    }

    dataBase.publicLiveData.observe(this, new Observer<List<TodoItem>>() {
      @Override
      public void onChanged(List<TodoItem> todoItems) {
        if(todoItems.size() == 0){
           adapter.addTodoListToAdapter(new ArrayList<>());
        }
        else{
          adapter.addTodoListToAdapter(new ArrayList<>(todoItems));
          //adapter.notifyDataSetChanged();
        }
      }
    });

    if (holder == null) {
      holder = new TodoItemsHolderImpl(recyclerView);
    }
    ArrayList<TodoItem> items = dataBase.getCopies();

    // Create the adapter
  //  adapter = new ToDoAdapterClass(this);
    // todo change recieve items from db
    //adapter.addTodoListToAdapter(holder.getCurrentItems());
    adapter.addTodoListToAdapter(items);

    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this,
            RecyclerView.VERTICAL, false));

    // TODO: implement the specs as defined below
    //    (find all UI components, hook them up, connect everything you need)
    EditText textInsertTask = findViewById(R.id.editTextInsertTask);
    FloatingActionButton buttonAddToDoItem = findViewById(R.id.buttonCreateTodoItem);

    adapter.setDeleteListener(new ToDoAdapterClass.DeleteClickListener() {
      @Override
      public void onDeleteClick(TodoItem item) {
        dataBase.deleteItem(item.getId());
      //  adapter.addTodoListToAdapter(dataBase.getCopies());
      }
    });

    adapter.setCheckBoxListener(new ToDoAdapterClass.CheckBoxListener() {
      @Override
      public void OnCheckBox(TodoItem item, boolean isChecked) {
          if (isChecked){
             dataBase.markAsDone(item.getId());
          }
          else {
              dataBase.markAsInProgress(item.getId());
          }
          adapter.addTodoListToAdapter(dataBase.getCopies());
      }
    });


    adapter.setEditOptionMenuListener(new ToDoAdapterClass.EditOptionListener() {
      @Override
      public void onEditOption(int position) {
        TodoItem item = holder.getCurrentItems().get(position);
       // holder.editItem(item, item.desc);
      }
    });

    adapter.setEditListener(new ToDoAdapterClass.EditClickListener() {
      @Override
      public void onEditClick(TodoItem item) {

        // open edit activity screen
        Intent editIntent = new Intent(MainActivity.this, EditActivity.class);

        // put the relevant data into the intent
        editIntent.putExtra("id", item.getId());
        startActivity(editIntent);
      }
    });

    buttonAddToDoItem.setOnClickListener(v -> {
        String textInsertTaskStr = textInsertTask.getText().toString();

        // If the text isn't empty, creates a new todo_ object
        if (!textInsertTaskStr.equals("")){
          dataBase.addItem(textInsertTaskStr, "IN_PROGRESS");
          ArrayList<TodoItem> list = dataBase.getCopies();
          adapter.addTodoListToAdapter(list);
          adapter.notifyDataSetChanged();
          textInsertTask.setText("");
        }
    });
  }

  // flip screen
  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable("saved_state", holder.saveState());
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    Serializable saved_output = savedInstanceState.getSerializable("saved_state");
    try {
      holder.loadState(saved_output);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    adapter.addTodoListToAdapter(dataBase.getCopies());
  }
}

/*

SPECS:

- the screen starts out empty (no items shown, edit-text input should be empty)
- every time the user taps the "add TODO item" button:
    * if the edit-text is empty (no input), nothing happens
    * if there is input:
        - a new TodoItem (checkbox not checked) will be created and added to the items list
        - the new TodoItem will be shown as the first item in the Recycler view
        - the edit-text input will be erased
- the "TodoItems" list is shown in the screen
  * every operation that creates/edits/deletes a TodoItem should immediately be shown in the UI
  * the order of the TodoItems in the UI is:
    - all IN-PROGRESS items are shown first. items are sorted by creation time,
      where the last-created item is the first item in the list
    - all DONE items are shown afterwards, no particular sort is needed (but try to think about what's the best UX for the user)
  * every item shows a checkbox and a description. you can decide to show other data as well (creation time, etc)
  * DONE items should show the checkbox as checked, and the description with a strike-through text
  * IN-PROGRESS items should show the checkbox as not checked, and the description text normal
  * upon click on the checkbox, flip the TodoItem's state (if was DONE will be IN-PROGRESS, and vice versa)
  * add a functionality to remove a TodoItem. either by a button, long-click or any other UX as you want
- when a screen rotation happens (user flips the screen):
  * the UI should still show the same list of TodoItems
  * the edit-text should store the same user-input (don't erase input upon screen change)

Remarks:
- you should use the `holder` field of the activity
- you will need to create a class extending from RecyclerView.Adapter and use it in this activity
- notice that you have the "row_todo_item.xml" file and you can use it in the adapter
- you should add tests to make sure your activity works as expected. take a look at file `MainActivityTest.java`



(optional, for advanced students:
- save the TodoItems list to file, so the list will still be in the same state even when app is killed and re-launched
)

*/
