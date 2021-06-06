package exercise.android.reemh.todo_items;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem implements Serializable {
    private String desc;
    private String status;
    private String itemId;
    private Date createdDate = null;
    private Date lastModifiedDate = null;

    // TODO: edit this class as you want
    public TodoItem(String id, String description, String status) {
        this.desc = description;
        this.status = status;
        this.createdDate = new Date();
        this.itemId = id;
        this.lastModifiedDate = new Date();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return itemId;
    }

    public String getDescription() {
        return desc;
    }

    public String getStatus() {
        return status;
    }

    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    public void setLastModifiedDate(Date date) {
        this.lastModifiedDate = date;
    }

    public void setId(String id) {
        this.itemId = id;
    }

    public String getCreatedDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        return dateFormat.format(this.createdDate);
    }

    public String getLastModifiedDate(){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
        return dateFormat.format(this.lastModifiedDate);
    }

    public Date getLastModifiedDateAsDate(){
        return new Date(String.valueOf(this.lastModifiedDate));
    }

    public Date getCreatedDateAsDate() {
        return new Date(String.valueOf(this.createdDate));
    }

    protected String itemStringRepresentation() {
        String repr = this.itemId + "/" +  this.desc + "/" + this.status + "/" + this.getCreatedDate()
                + "/" + this.getLastModifiedDate();
        return repr;
    }

    static protected TodoItem stringToToDoItem(String stringRepr) {
        if (stringRepr == null) return null;
        try {
            String val = stringRepr;
            String[] split = val.split("/");
            String id = split[0];
            String desc = split[1];
            String status = split[2];
            String createdDate = split[3];
            String lastModifiedDate = split[4];

            // create a new item
            TodoItem newItem = new TodoItem(id, desc, status);
            SimpleDateFormat convertedDate = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            Date date1 = convertedDate.parse(createdDate);
            Date date2 = convertedDate.parse(lastModifiedDate);
            newItem.setCreatedDate(date1);
            newItem.setLastModifiedDate(date2);
            return newItem;
        } catch (Exception e) {
            System.out.println("exception: " + e.toString() + "input: " + stringRepr);
            return null;
        }
    }
}
