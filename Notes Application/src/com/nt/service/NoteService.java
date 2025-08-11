package com.nt.service;


import com.nt.model.Note;
import com.nt.storage.FileStorage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for notes: add, list, find, delete.
 */
public class NoteService {

    private final String filePath;
    private int nextId = 1;

    public NoteService(String filePath) {
        this.filePath = filePath;
        try {
            List<Note> existing = FileStorage.readAll(filePath);
            // compute nextId as (max id + 1) to avoid collisions after restarts
            Optional<Integer> maxId = existing.stream().map(Note::getId).max(Comparator.naturalOrder());
            if (maxId.isPresent()) nextId = maxId.get() + 1;
        } catch (IOException e) {
            // file may not exist yet; we'll create when needed
        }
    }

    // list all notes
    public List<Note> listNotes() {
        try {
            return FileStorage.readAll(filePath);
        } catch (IOException e) {
            return new ArrayList<>(); // return empty list on error
        }
    }

    // add a new note; returns the created Note
    public Note addNote(String title, String content) throws IOException {
        Note n = new Note(nextId++, title, content, LocalDateTime.now());
        FileStorage.append(filePath, n);
        return n;
    }

    // find by id
    public Note findById(int id) {
        List<Note> all = listNotes();
        return all.stream().filter(n -> n.getId() == id).findFirst().orElse(null);
    }

    // delete by id; returns true if deleted
    public boolean deleteById(int id) throws IOException {
        List<Note> all = listNotes();
        boolean removed = all.removeIf(n -> n.getId() == id);
        if (removed) {
            FileStorage.writeAll(filePath, all); // rewrite file
        }
        return removed;
    }
}
