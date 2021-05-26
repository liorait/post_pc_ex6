package exercise.android.reemh.todo_items;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.Date;

public class TodoItem implements Serializable {
    protected String desc;
    protected String status;
    private boolean isDone;
    protected Date createdDate = new Date();


  // TODO: edit this class as you want
    public TodoItem(String description, String status){
        this.desc = description;
        this.status = status;
        this.createdDate = new Date();
        isDone = false;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public void setDescription(String desc){
        this.desc = desc;
    }
    public String getCreatedDate(){
        String date = this.createdDate.toString();
        return date;
    }
    public Date getCreatedDateAsDate(){
        return new Date(String.valueOf(this.createdDate));
    }

}
