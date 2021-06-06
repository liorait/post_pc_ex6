package exercise.android.reemh.todo_items;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.mockito.internal.matchers.Null;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends Activity {

    public LocalDataBase dataBase = null;
    private ArrayList<TodoItem> items = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        if (dataBase == null){
            dataBase = ToDoItemsApplication.getInstance().getDataBase();
        }

        // finds all the views
        EditText editText = findViewById(R.id.editTextDescription);
        TextView createdDate = findViewById(R.id.createdDateTextView);
        TextView lastModifiedView = findViewById(R.id.lastModifiedtextView);
        TextView statusView = findViewById(R.id.statustextView);
        CheckBox checkBoxStatus = findViewById(R.id.checkBoxChangeStatus);
        editText.setEnabled(true);

        items = dataBase.getCopies();
        Intent openIntent = getIntent();
        TodoItem currentItem = null;

        if (openIntent.hasExtra("id")){
            String itemId = openIntent.getStringExtra("id");

            for (TodoItem item: items){
                if (item.getId().equals(itemId)){
                    currentItem = item;
                    break;
                }
            }

            if (currentItem == null){
                return;
            }

            editText.setText(currentItem.getDescription()); // Set the current text. Enables to edit the text
            createdDate.setText(currentItem.getCreatedDate());

           // @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
           // Date lastModified = currentItem.getLastModifiedDateAsDate();
         //   String lastModifiedStr = currentItem.getLastModifiedDate();
          //  Date lastModified = dataBase.getById(itemId).getLastModifiedDateAsDate();
            Date lastModified = currentItem.getLastModifiedDateAsDate();
            if (lastModified != null){
                Date currentDate = new Date();
                String result = getTimeDifference(currentDate, lastModified);
                lastModifiedView.setText(result);
            }

           // lastModifiedView.setText(dateFormat.format(lastModified));
            //lastModifiedView.setText(lastModifiedStr);
            statusView.setText(currentItem.getStatus());

            if (currentItem.getStatus().equals("IN_PROGRESS")){
                checkBoxStatus.setChecked(false);
            }
            else{
                checkBoxStatus.setChecked(true);
            }
            checkBoxStatus.setEnabled(true);

        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String changedText = editText.getText().toString();
                String itemId = openIntent.getStringExtra("id");
                dataBase.editDescription(itemId, changedText);
                dataBase.editLastModifiedDate(itemId, new Date());

                // show the last modified date on screen
                // show the last modified date on screen
                Date lastModified = dataBase.getById(itemId).getLastModifiedDateAsDate();

                if (lastModified != null) {
                    Date currentDate = new Date();
                    String result = getTimeDifference(currentDate, lastModified);
                    lastModifiedView.setText(result);
                }
            }
        });
        checkBoxStatus.setOnClickListener(v -> {
            String itemId = openIntent.getStringExtra("id");

            if (checkBoxStatus.isChecked()){
                statusView.setText("DONE");
                dataBase.markAsDone(itemId);
                dataBase.editLastModifiedDate(itemId, new Date());

                // show the last modified date on screen
                Date lastModified = dataBase.getById(itemId).getLastModifiedDateAsDate();
                if (lastModified != null) {
                    Date currentDate = new Date();
                    String result = getTimeDifference(currentDate, lastModified);
                    lastModifiedView.setText(result);
                }
            }
            else{
                statusView.setText("IN_PROGRESS");
                dataBase.markAsInProgress(itemId);
                dataBase.editLastModifiedDate(itemId, new Date());

                // show the last modified date on screen
                Date lastModified = dataBase.getById(itemId).getLastModifiedDateAsDate();
                if (lastModified != null) {
                    Date currentDate = new Date();
                    String result = getTimeDifference(currentDate, lastModified);
                    lastModifiedView.setText(result);
                }
            }
        });
    }
    private String getTimeDifference(Date currentDate, Date lastModified){

        long difference_mili = currentDate.getTime() - lastModified.getTime(); // miliseconds
        long difference_seconds = (difference_mili/1000) % 60;
        long difference_minutes = (difference_mili/(1000 * 60)) % 60;
        long difference_hours = (difference_mili/(1000 * 60 * 60)) % 24;
        long difference_years = (difference_mili / (1000L * 60 * 60 * 24 * 365));
        long difference_days = (difference_mili / (1000 * 60 * 60 * 24)) % 365;

        // was less than a hour ago
        if (difference_years == 0 && difference_days == 0 && difference_hours == 0 && ((difference_minutes <= 60) || (difference_seconds <=60))){
            return difference_minutes + " minutes ago";
        }
        // was earlier than a hour, but today
        else if (difference_years == 0 && difference_days == 0 && difference_hours != 0){
            long hours = lastModified.getHours();
            long minutes = lastModified.getMinutes();
            String time = hours+":"+minutes;
            return "Today at " + time;
        }
        // was yesterday or earlier
        else if (difference_years == 0 && difference_days != 0){
            long hours = lastModified.getHours();
            long minutes = lastModified.getMinutes();
            String time = hours+":"+minutes;
            return lastModified.toString() + " at " + time;
        }
       return null;
    }

}
