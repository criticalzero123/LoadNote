package com.loadnote.landingpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loadnote.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> implements Filterable {
    private List<String> names = new ArrayList<>();
    private List<String> namesFilter ;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item,parent,false);
        return new NoteHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteHolder holder, int position) {
        holder.textViewName.setText(names.get(position));
    }



    @Override
    public int getItemCount() {
        return names.size();
    }

    public String name(int pos){
        return names.get(pos);
    }


    public void setNames(List<String> names){
        this.names = names;
        namesFilter = new ArrayList<>(names);
        notifyDataSetChanged();
    }

    public String getNameAt(int pos){
        return names.get(pos);
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(namesFilter);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(String name: namesFilter){
                    if (name.toLowerCase().contains(filterPattern)){
                        filteredList.add(name);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            names.clear();
            names.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class NoteHolder extends  RecyclerView.ViewHolder{
        private TextView textViewName;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
