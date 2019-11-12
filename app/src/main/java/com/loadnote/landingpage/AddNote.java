package com.loadnote.landingpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.loadnote.R;

import java.util.Objects;

public class AddNote extends AppCompatActivity {
    public static final String EXTRA_NAME =
            "com.loadnote.landingpage.EXTRA_NAME";
    public static final String EXTRA_DEBIT =
            "com.loadnote.landingpage.EXTRA_DEBIT";
    public static final String EXTRA_LOAD =
            "com.loadnote.landingpage.EXTRA_LOAD";
    public static final String EXTRA_DATE =
            "com.loadnote.landingpage.EXTRA_DATE";
    private EditText editTextName;
    private EditText editTextDebit;
    private EditText editTextLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextName = findViewById(R.id.edit_text_name);
        editTextDebit = findViewById(R.id.edit_text_debit);
        editTextLoad = findViewById(R.id.edit_text_load);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");
    }

    private void saveNote(){
        Intent data = new Intent();
        String name = editTextName.getText().toString();
        String tDebit = editTextDebit.getText().toString();
        String tLoad = editTextLoad.getText().toString();


        if(name.trim().isEmpty() && tDebit.trim().isEmpty() && tLoad.trim().isEmpty()){
            Toast.makeText(this, "Please Insert Name, Amount and Promo", Toast.LENGTH_SHORT).show();
        } else if (name.trim().isEmpty()){
            Toast.makeText(this, "Please Insert Name", Toast.LENGTH_SHORT).show();
        } else if (tDebit.trim().isEmpty()){
            Toast.makeText(this, "Please Insert Amount", Toast.LENGTH_SHORT).show();
        } else if (tLoad.trim().isEmpty()){
            Toast.makeText(this, "Please Insert Promo", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Long time = System.currentTimeMillis();
                name = name.trim();
                String fName = name.substring(0,1).toUpperCase() + name.substring(1);
                String fLoad = tLoad.substring(0,1).toUpperCase() + tLoad.substring(1) + " " + tDebit;
                int debit = Integer.parseInt(tDebit);
                if (debit > 0) {

                    debit += 3;

                    data.putExtra(EXTRA_NAME, fName);
                    data.putExtra(EXTRA_DEBIT, debit);
                    data.putExtra(EXTRA_LOAD,fLoad);
                    data.putExtra(EXTRA_DATE, time);

                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(this, "Please Enter A Valid Amount", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                Toast.makeText(AddNote.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
