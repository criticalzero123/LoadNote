package com.loadnote.landingpage;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table WHERE name = :name")
    void deletePerson(String name);

    @Query("SELECT DISTINCT name FROM note_table ORDER BY name ASC")
    LiveData<List<String>> getName();


    @Query("SELECT SUM(debit) FROM note_table")
    LiveData<Integer> total_debit();

    @Query("SELECT * FROM note_table WHERE name = :name")
    List<Note> findPerson(String name);

    @Query("SELECT SUM(debit) FROM note_table WHERE name = :name")
    Integer total_person_debit(String name);


}
