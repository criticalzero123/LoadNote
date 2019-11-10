package com.loadnote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loadnote.landingpage.AddNote;
import com.loadnote.landingpage.Converters;
import com.loadnote.landingpage.Note;
import com.loadnote.landingpage.NoteAdapter;
import com.loadnote.landingpage.NoteViewModel;
import com.loadnote.person.PersonNote;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditorPreferences;

    private String personName;

    private NoteViewModel noteViewModel;
    private NoteViewModel paymentViewModel;
    private TextView total_debit;
    private NoteAdapter adapter;
    private static int totalCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //setting the adapter
        adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);


        //for the payment
        paymentViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        //for the name
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllName().observe(this, strings -> adapter.setNames(strings));

        //for the total
        total_debit = findViewById(R.id.total_debit);
        NoteViewModel totalDebitViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        totalDebitViewModel.getTotal_debit().observe(this, integer -> total_debit.setText((integer == null) ? "0" : Integer.toString(integer)));





        //for delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                SweetAlertDialog alertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                alertDialog.setCanceledOnTouchOutside(false);

                        personName = adapter.getNameAt(viewHolder.getAdapterPosition());

                        alertDialog.setTitleText("Are you sure?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                payment();

                                noteViewModel.deletePerson(adapter.getNameAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(MainActivity.this, "Person deleted", Toast.LENGTH_SHORT).show();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                noteViewModel.getAllName().observe(MainActivity.this, strings -> adapter.setNames(strings));
                                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        //when clicked name
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                Intent intent = new Intent(MainActivity.this, PersonNote.class);
                intent.putExtra(PersonNote.EXTRA_NAME, adapter.name(pos));
                startActivity(intent);
            }
        });
    }

    private void payment() {
        paymentViewModel.setTotalDebitPerson(this.getPersonName());

        Toast.makeText(this, this.getPersonName(), Toast.LENGTH_SHORT).show();
        //TODO: sa totalCollect i butang ang ge delete na person
        //totalCollect =

        //for the Payment
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditorPreferences = mPreferences.edit();

        //getting sa total collect na ni exist if wala ang defValue maoy ma butang
        String total = mPreferences.getString("com.loadnote.COLLECTED", ""+0);

        //adding ang ge pang delete
        totalCollect += Integer.parseInt(total);

        mEditorPreferences.putString("com.loadnote.COLLECTED", ""+totalCollect);

        mEditorPreferences.apply();

    }


    //when click the add note
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddNote.EXTRA_NAME);
            String loadType = data.getStringExtra(AddNote.EXTRA_LOAD);
            int debit = data.getIntExtra(AddNote.EXTRA_DEBIT, 3);
            Long time = data.getLongExtra(AddNote.EXTRA_DATE,0);
            Date date = Converters.fromTimestamp(time);

            Note note = new Note(name, debit,loadType);
            note.setDate(date);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    //bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.note:
                    note();
                    return true;

                case R.id.add:
                    Intent intent1 = new Intent(MainActivity.this, AddNote.class);
                    startActivityForResult(intent1, ADD_NOTE_REQUEST);
                    return true;

                case R.id.payment:
                    Intent intent2 = new Intent(MainActivity.this, CashTotal.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    private void note(){
        ArrayList<HashMap<String,Object>> items =new ArrayList<HashMap<String,Object>>();

        final PackageManager pm = getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs) {
            if( pi.packageName.toLowerCase().contains("notes")){
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
            Toast.makeText(this, "Notes not found!", Toast.LENGTH_SHORT).show();
        }
    }

    //search function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search here...");
        searchView.onActionViewExpanded();
        searchView.setFocusable(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    private String getPersonName(){
        return personName;
    }


}

