package com.nt.main;

import com.nt.model.Note;
import com.nt.service.NoteService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // data file location relative to project root
        String dataFile = "data/notes.txt";

        // create the service which manages reading/writing notes
        NoteService service = new NoteService(dataFile);

        // scanner for user input
        Scanner scanner = new Scanner(System.in);

        // main loop flag — keeps the menu running until user exits
        boolean loopRunning = true;

        // menu loop
        while (loopRunning) {
            // display menu
            System.out.println("\n====== Simple Notes Manager ======");
            System.out.println("1. Add note");
            System.out.println("2. List notes");
            System.out.println("3. View note by id");
            System.out.println("4. Delete note");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": // Add note
                    System.out.print("Title: ");
                    String title = scanner.nextLine().trim();
                    System.out.println("Content (enter blank line to finish):");
                    StringBuilder contentBuilder = new StringBuilder();
                    String line;
                    // read multiple lines until an empty line
                    while (!(line = scanner.nextLine()).isEmpty()) {
                        contentBuilder.append(line).append("\n");
                    }
                    String content = contentBuilder.toString().trim();
                    try {
                        Note created = service.addNote(title, content);
                        System.out.println("Saved: " + created);
                    } catch (IOException e) {
                        System.err.println("Error saving note: " + e.getMessage());
                    }
                    break;

                case "2": // List notes
                    List<Note> notes = service.listNotes();
                    if (notes.isEmpty()) {
                        System.out.println("No notes found.");
                    } else {
                        notes.forEach(n -> System.out.println(n.getId() + " | " + n.getTitle() + " | " + n.getCreatedAt()));
                    }
                    break;

                case "3": // View note by id
                    System.out.print("Enter note id: ");
                    String idStr = scanner.nextLine();
                    try {
                        int id = Integer.parseInt(idStr);
                        Note found = service.findById(id);
                        if (found == null) {
                            System.out.println("Note not found for id: " + id);
                        } else {
                            System.out.println("\n--- " + found.getTitle() + " ---");
                            System.out.println(found.getContent());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid id. Please enter a number.");
                    }
                    break;

                case "4": // Delete note
                    System.out.print("Enter note id to delete: ");
                    String delIdStr = scanner.nextLine();
                    try {
                        int id = Integer.parseInt(delIdStr);
                        boolean ok = service.deleteById(id);
                        if (ok) System.out.println("Deleted note id: " + id);
                        else System.out.println("Note not found: " + id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid id. Please enter a number.");
                    } catch (IOException e) {
                        System.err.println("Error deleting note: " + e.getMessage());
                    }
                    break;

                case "5": // Exit
                    System.out.println("Thank you for using Notes Manager. Goodbye!");
                    loopRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Enter 1-5.");
            }
        }

        scanner.close();
    }
}
