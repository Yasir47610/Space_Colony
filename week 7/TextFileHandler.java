package main;

import java.io.*;
import java.util.*;

class TextFileHandler implements FileHandler {
    public void saveToFile(String filename, List<Item> items) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Item item : items) {
                if (item instanceof Book) {
                    writer.println("BOOK;" + item.getId() + ";" + item.getTitle() + ";" + ((Book) item).getAuthor());
                } else if (item instanceof DVD) {
                    writer.println("DVD;" + item.getId() + ";" + item.getTitle() + ";" + ((DVD) item).getDuration());
                }
            }
            System.out.println("Items saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }

    public List<Item> loadFromFile(String filename) {
        List<Item> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals("BOOK")) {
                    items.add(new Book(parts[1], parts[2], parts[3]));
                } else if (parts[0].equals("DVD")) {
                    items.add(new DVD(parts[1], parts[2], Integer.parseInt(parts[3])));
                }
            }
            System.out.println("Items loaded from file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading from file.");
        }
        return items;
    }
}
