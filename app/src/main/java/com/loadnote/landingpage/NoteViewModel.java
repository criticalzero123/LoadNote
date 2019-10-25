package com.loadnote.landingpage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<String>> allName;
    private LiveData<Integer> total_debit;
    private MutableLiveData<List<Note>> searchResults;
    private MutableLiveData<Integer> totalDebitPerson;


    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allName = repository.getAllName();
        total_debit = repository.getTotal_debit();
        searchResults = repository.getSearchResults();
        totalDebitPerson = repository.getTotalDebitPerson();
    }

    public void insert(Note note){
        repository.insert(note);
    }


   public MutableLiveData<List<Note>> getSearchResults() {
        return searchResults;
    }
    public MutableLiveData<Integer> getTotalDebitPerson() {
        return totalDebitPerson;
    }


    public LiveData<List<String>> getAllName() {
        return allName;
    }

    public LiveData<Integer> getTotal_debit() {
        return total_debit;
    }


    public void findName(String name) {
        repository.findName(name);
    }

    public void setTotalDebitPerson(String name){
        repository.totalDebitPerson(name);
    }

    public void deletePerson(String name) {
        repository.deletePerson(name);
    }

    public void delete(Note note){
        repository.delete(note);
    }
}
