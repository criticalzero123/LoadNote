package com.loadnote.person;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loadnote.R;
import com.loadnote.landingpage.Note;
import com.loadnote.landingpage.NoteViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PersonNote extends AppCompatActivity {
    public static final String EXTRA_NAME =
            "com.loadnote.person.EXTRA_NAME";


    private NoteViewModel viewModel;
    private NoteViewModel totalDebitViewModel;
    private PersonAdapter adapter;
    private TextView total;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_note);

        total = findViewById(R.id.total_person_debit);

        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        totalDebitViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        adapter = new PersonAdapter();
        recyclerView = findViewById(R.id.recycler_view_person);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                SweetAlertDialog alertDialog = new SweetAlertDialog(PersonNote.this, SweetAlertDialog.WARNING_TYPE);
                alertDialog.setCanceledOnTouchOutside(false);

                alertDialog.setTitleText("Are you sure to Delete?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                viewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(PersonNote.this, "Note deleted", Toast.LENGTH_SHORT).show();
                                sDialog.dismissWithAnimation();

                                //refresh the activity
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                viewModel.getSearchResults().observe(PersonNote.this, notes -> adapter.setNotes(notes));
                                Toast.makeText(PersonNote.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);


        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_NAME);

        viewModel.findName(name);

        totalDebitViewModel.setTotalDebitPerson(name);

        totalDebitViewModel.getTotalDebitPerson().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                total.setText(Integer.toString(integer));
            }
        });

        viewModel.getSearchResults().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.calculator, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.calculator) {

            ArrayList<HashMap<String,Object>> items =new ArrayList<HashMap<String,Object>>();

            final PackageManager pm = getPackageManager();
            List<PackageInfo> packs = pm.getInstalledPackages(0);
            for (PackageInfo pi : packs) {
                if( pi.packageName.toLowerCase().contains("calcul")){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("appName", pi.applicationInfo.loadLabel(pm));
                    map.put("packageName", pi.packageName);
                    items.add(map);
                }
            }

            if(items.size()>=1){
                String packageName = (String) items.get(0).get("packageName");
                Intent i = pm.getLaunchIntentForPackage(Objects.requireNonNull(packageName));
                if (i != null)
                    startActivity(i);
            }
            else{
                Toast.makeText(this, "Calculator not found!", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
