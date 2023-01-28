package com.example.noteapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NotesViewModel extends ViewModel {

    private NotesRepository mRepository;

    private LiveData<List<Note>> notes;

    public NotesViewModel () {
        mRepository = NotesRepository.getNotesRepository();
        notes = mRepository.getAll();
    }

    public LiveData<List<Note>> get() { return notes; }

    public void add(Note note) { mRepository.add(note); }

    public void delete(Note note) { mRepository.delete(note); }

    public void updateNote(Note note) {mRepository.updateNote(note);}
}
