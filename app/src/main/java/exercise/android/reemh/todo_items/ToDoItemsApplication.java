package exercise.android.reemh.todo_items;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

// Application is a singleton
public class ToDoItemsApplication extends Application {

    private LocalDataBase dataBase;
    private static ToDoItemsApplication instance = null;

    public LocalDataBase getDataBase(){
        return dataBase;
    }

    public static ToDoItemsApplication getInstance(){
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataBase = new LocalDataBase(this); // pass the current context to allow broadcasts
    }
}
