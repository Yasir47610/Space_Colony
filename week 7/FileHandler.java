package main;

import java.util.List;

public interface FileHandler {
    void saveToFile(String filename, List<Item> items);
    List<Item> loadFromFile(String filename);
}
