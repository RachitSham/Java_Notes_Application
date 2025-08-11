package com.nt.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note {

    // fields
    private int id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    // formatter used for serializing timestamp
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // no -arg constructors
    public Note() {

    }

    public Note(int id, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Escape delimiter and newlines so we can store the note on one line.
     */
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("|", "\\|").replace("\n", "\\n");
    }

    /**
     * Unescape sequences back to original text.
     */
    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\n", "\n").replace("\\|", "|");
    }

    /**
     * Convert this Note to a single-line record for file storage:
     * id|createdAt|title|content
     */
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append('|');
        sb.append(createdAt.format(FMT)).append('|');
        sb.append(escape(title)).append('|');
        sb.append(escape(content));
        return sb.toString();
    }

    /**
     * Parse a single-line file record back to a Note object.
     * Returns null if parsing fails.
     */
    public static Note fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        // split only on unescaped '|' — for simplicity assume escape never introduces '|'
        // we will split on '|' and then unescape
        String[] parts = line.split("\\|", 4); // limit to 4 parts
        if (parts.length < 4) return null;
        try {
            int id = Integer.parseInt(parts[0]);
            LocalDateTime created = LocalDateTime.parse(parts[1], FMT);
            String title = unescape(parts[2]);
            String content = unescape(parts[3]);
            return new Note(id, title, content, created);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Note[" + id + "] " + title + " (" + createdAt.format(FMT) + ")";
    }
}
