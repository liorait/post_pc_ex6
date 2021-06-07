package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LocalDataBase {
    private final ArrayList<TodoItem> items = new ArrayList<>();
    private final Context context;
    private final SharedPreferences sp;
    private final MutableLiveData<List<TodoItem>> mutableLiveData = new MutableLiveData<>();
    public final LiveData<List<TodoItem>> publicLiveData = mutableLiveData;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LocalDataBase(Context context){
        this.context = context;
        sortItems();
        this.sp = context.getSharedPreferences("local_db_todo_items", Context.MODE_PRIVATE);
        initializeSp();
    }

    // initialize the shared preferences
    private void initializeSp(){
        Set<String> keys = sp.getAll().keySet();
        for (String key: keys){
            String stringRepr = sp.getString(key, null);
            TodoItem item = TodoItem.stringToToDoItem(stringRepr);
            if (item != null){
                items.add(item);
            }
        }
        // update the live data about the changes
        mutableLiveData.setValue(new ArrayList<>(items));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<TodoItem> getCopies(){
        sortItems();
        return new ArrayList<>(items);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addItem(String description, String state){
        String newId = UUID.randomUUID().toString();
        TodoItem newItem = new TodoItem(newId, description, state);
        items.add(0, newItem);
        sortItems();

        // update sp of the changes
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(newItem.getId(), newItem.itemStringRepresentation());
        editor.apply();

        // update the live data of the changed
        mutableLiveData.setValue(new ArrayList<>(items));
        sendBroadcastDbChanged(); // send broadcast
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendBroadcastDbChanged(){
        Intent broadcast = new Intent("changed_db");
        broadcast.putExtra("new_list", getCopies());
        context.sendBroadcast(broadcast);
    }

    public @Nullable TodoItem getById(String id){
        if (id == null) return null;
        for (TodoItem item : items){
            if (item.getId().equals(id)){
                return item;
            }
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void markAsDone(String itemId){

        TodoItem newItem = null;
        for (TodoItem item : items){
            if (item.getId().equals(itemId)){
                newItem = new TodoItem(item.getId(),  item.getDescription(), item.getStatus());
                newItem.setStatus("DONE");
                newItem.setCreatedDate(item.getCreatedDateAsDate());
                items.add(this.items.size(), newItem);
                items.remove(item);
                break;
            }
        }
        sortItems();

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(newItem.getId(), newItem.itemStringRepresentation());
        editor.apply();

        mutableLiveData.setValue(new ArrayList<>(items));
        sendBroadcastDbChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void markAsInProgress(String itemId){
        TodoItem newItem = null;
        for (TodoItem item : items){
            if (item.getId().equals(itemId)){
                newItem = new TodoItem(item.getId(), item.getDescription(), item.getStatus());
                newItem.setStatus("IN_PROGRESS");
                newItem.setCreatedDate(item.getCreatedDateAsDate());
                items.add(0, newItem);
                items.remove(item);
                break;
            }
        }
        sortItems();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(newItem.getId(), newItem.itemStringRepresentation());
        editor.apply();
        mutableLiveData.setValue(new ArrayList<>(items));
        sendBroadcastDbChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortItems(){
        this.items.sort(new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                if (o1.getStatus().equals("IN_PROGRESS") && o2.getStatus().equals("IN_PROGRESS")) {
                    return o2.getCreatedDate().compareTo(o1.getCreatedDate());
                }
                return 0;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editDescription(String itemId, String newDescription){

        TodoItem itemToEdit = getById(itemId);
        if (itemToEdit == null) return;

        TodoItem newItem = new TodoItem(itemToEdit.getId(), newDescription, itemToEdit.getStatus());
        newItem.setCreatedDate(itemToEdit.getCreatedDateAsDate());
        items.add(newItem); // add depend on status 0 or size
        items.remove(itemToEdit);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(newItem.getId(), newItem.itemStringRepresentation());
        editor.apply();
        mutableLiveData.setValue(new ArrayList<>(items));
        sendBroadcastDbChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editLastModifiedDate(String itemId, Date date){
        TodoItem itemToEdit = getById(itemId);
        if (itemToEdit == null) return;
        TodoItem newItem = new TodoItem(itemToEdit.getId(), itemToEdit.getDescription(), itemToEdit.getStatus());
        newItem.setCreatedDate(itemToEdit.getCreatedDateAsDate());
        newItem.setLastModifiedDate(date);
        items.add(newItem);
        items.remove(itemToEdit);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(newItem.getId(), newItem.itemStringRepresentation());
        editor.apply();
        mutableLiveData.setValue(new ArrayList<>(items));
        sendBroadcastDbChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteItem(String itemId){
        TodoItem toDelete = null;
        for (TodoItem item : items){
            if (item.getId().equals(itemId)){
                toDelete = item;
                break;
            }
        }
        if (toDelete != null){
            items.remove(toDelete);
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.remove(toDelete.getId()); // remove the key
        editor.apply();

        mutableLiveData.setValue(new ArrayList<>(items));
        sendBroadcastDbChanged();
    }
}
