package com.nt.storage;


import com.nt.model.Note;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing notes file.
 */
public class FileStorage {

    // Ensure the file exists, create parent directories as needed
    public static void ensureFileExists(String path) throws IOException {
        Path p = Path.of(path);
        if (!Files.exists(p)) {
            Files.createDirectories(p.getParent() != null ? p.getParent() : Path.of("."));
            Files.createFile(p);
        }
    }

    // Read all notes from file (each line => one note)
    public static List<Note> readAll(String path) throws IOException {
        ensureFileExists(path);
        List<Note> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                Note n = Note.fromFileString(line);
                if (n != null) result.add(n);
            }
        }
        return result;
    }

    // Write the entire notes list to file (overwrite)
    public static void writeAll(String path, List<Note> notes) throws IOException {
        ensureFileExists(path);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, false))) {
            for (Note n : notes) {
                bw.write(n.toFileString());
                bw.newLine();
            }
            bw.flush();
        }
    }

    // Append a single note to the file
    public static void append(String path, Note note) throws IOException {
        ensureFileExists(path);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(note.toFileString());
            bw.newLine();
            bw.flush();
        }
    }
}
