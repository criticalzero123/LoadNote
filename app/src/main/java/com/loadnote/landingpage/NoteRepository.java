package com.loadnote.landingpage;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NoteRepository  {
    private NoteDao noteDao;
    private LiveData<List<String>> allName;
    private LiveData<Integer> total_debit;
    private MutableLiveData<List<Note>> searchResults = new MutableLiveData<>();
    private MutableLiveData<Integer> totalDebitPerson = new MutableLiveData<>();

    private void asyncFinished(List<Note> results) {
        searchResults.setValue(results);
    }

    private void total(Integer results) {
        totalDebitPerson.setValue(results);
    }

    public NoteRepository(Application application) {
        NoteDataBase database = NoteDataBase.getInstance(application);
        noteDao = database.noteDao();
        allName = noteDao.getName();
        total_debit = noteDao.total_debit();
    }

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<String>> getAllName() {
        return allName;
    }


    public LiveData<Integer> getTotal_debit() {
        return total_debit;
    }


    public MutableLiveData<List<Note>> getSearchResults() {
        return searchResults;
    }

    public MutableLiveData<Integer> getTotalDebitPerson(){
        return totalDebitPerson;
    }


    public void findName(String name) {
        NameQueryAsyncTask task = new NameQueryAsyncTask(noteDao);
        task.person = this;
        task.execute(name);
    }

    public void totalDebitPerson(String name){
        TotalQueryAsyncTask task = new TotalQueryAsyncTask (noteDao);
        task.total = this;
        task.execute(name);
    }

    public void deletePerson(String name) {
        DeleteAsyncTask task = new DeleteAsyncTask(noteDao);
        task.execute(name);
    }




    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {

        private NoteDao noteDao;

        DeleteAsyncTask(NoteDao dao) {
            noteDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            noteDao.deletePerson(params[0]);
            return null;
        }
    }

    private static class TotalQueryAsyncTask extends AsyncTask<String, Void, Integer> {
        private NoteDao noteDao;
        private NoteRepository total = null;

        TotalQueryAsyncTask(NoteDao dao) {
            noteDao = dao;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return noteDao.total_person_debit(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            total.total(result);
        }
    }

    private static class NameQueryAsyncTask extends AsyncTask<String, Void, List<Note>> {

        private NoteDao asyncTaskDao;
        private NoteRepository person = null;

        NameQueryAsyncTask(NoteDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<Note> doInBackground(final String... params) {
            return asyncTaskDao.findPerson(params[0]);
        }

        @Override
        protected void onPostExecute(List<Note> result) {
            person.asyncFinished(result);
        }
    }


    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
}
