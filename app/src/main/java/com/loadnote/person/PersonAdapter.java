package com.loadnote.person;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadnote.R;
import com.loadnote.landingpage.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder>  {
    private List<Note> notes = new ArrayList<>();


    @NonNull
    @Override
    public PersonAdapter.PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_person,parent,false);
        return new PersonHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonHolder holder, int position) {
        TextView name = holder.textViewName;
        TextView debit = holder.textViewDebit;
        TextView load = holder.textViewLoad;
        TextView time = holder.textViewTime;

        name.setText(""+notes.get(position).getName());
        debit.setText(""+notes.get(position).getDebit());
        load.setText(""+notes.get(position).getLoad());
        time.setText(""+dateFormat(notes.get(position).getDate()));

    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int pos){
        return notes.get(pos);
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class PersonHolder  extends  RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDebit;
        private TextView textViewLoad;
        private TextView textViewTime;

        public PersonHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name_person);
            textViewDebit = itemView.findViewById(R.id.text_view_debit_person);
            textViewLoad = itemView.findViewById(R.id.text_view_load);
            textViewTime = itemView.findViewById(R.id.text_view_date);
        }
    }

    public String dateFormat(Date date){

        String pattern = "dd-MMM-yyyy h:m a";
        SimpleDateFormat formatDate = new SimpleDateFormat(pattern);

        return formatDate.format(date);
    }
}
