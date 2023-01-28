package com.example.noteapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note")
    List<Note> get();

    @Insert
    void insert(Note... notes);

    @Insert
    void insertList(List<Note> notes);

    @Update
    void update(Note... notes);

    @Delete
    void delete(Note... notes);

    @Query("DELETE FROM Note")
    void clear();
}
