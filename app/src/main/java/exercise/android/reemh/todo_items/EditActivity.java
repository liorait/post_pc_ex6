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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
       // String itemId = null;
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

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
            Date lastModified = new Date();

            lastModifiedView.setText(dateFormat.format(lastModified)); // todo change
            statusView.setText(currentItem.getStatus()); // todo make gettes settrs to item

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
            }
        });
        checkBoxStatus.setOnClickListener(v -> {
            String itemId = openIntent.getStringExtra("id");

            if (checkBoxStatus.isChecked()){
                statusView.setText("DONE");
                dataBase.markAsDone(itemId);
            }
            else{
                statusView.setText("IN_PROGRESS");
                dataBase.markAsInProgress(itemId);
            }
        });
    }

}
