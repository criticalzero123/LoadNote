package com.loadnote.landingpage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String load;

    private int debit;

    private Date date;

    public Note(String name, int debit, String load) {
        this.name = name;
        this.debit = debit;
        this.load = load;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getDebit() {
        return debit;
    }

    public String getLoad() {
        return load;
    }

    public void setId(int id) {
        this.id = id;
    }
}
