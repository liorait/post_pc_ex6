package exercise.android.reemh.todo_items;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataBase = new LocalDataBase(this); // pass the current context to allow broadcasts
    }
}
