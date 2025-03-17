package main;

import java.util.*;

class LibrarySystem {
    private List<Item> items = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private FileHandler fileHandler = new TextFileHandler();
    private boolean running = true;
    
    public void displayMenu() {
        while (running) {
            System.out.println("=== Library Management System ===");
            System.out.println("1. Add new Book");
            System.out.println("2. Add new DVD");
            System.out.println("3. Remove item");
            System.out.println("4. List all items");
            System.out.println("5. Search by title");
            System.out.println("6. Save to file");
            System.out.println("7. Load from file");
            System.out.println("8. Exit");
            System.out.print("Enter your choice:\n");
    
            // Check for invalid input
            if (!scanner.hasNextInt()) {
                scanner.next(); // Clear the invalid input
                System.out.println("Invalid choice. Please try again.");
                continue;
            }
    
            int choice = scanner.nextInt();
        
    
            switch (choice) {
                case 1: scanner.nextLine(); addItem(true);  break; 
                case 2: scanner.nextLine();addItem(false); break;
                case 3: scanner.nextLine();removeItem(); break;
                case 4: scanner.nextLine();listAllItems(); break;
                case 5: scanner.nextLine();searchByTitle(); break;
                case 6: scanner.nextLine();saveToFile(); break;
                case 7: scanner.nextLine();loadFromFile(); break;
                case 8: 
                    System.out.println("Goodbye!"); 
                    running = false;
                    break;
                default: 
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            System.out.println();
            
        }
    }
    
    
    
    private void addItem(boolean isBook) {
        if (isBook) {
            System.out.print("Enter book ID:\n");
            String id = scanner.nextLine();
            System.out.print("Enter title:\n");
            String title = scanner.nextLine();
            System.out.print("Enter author:\n");
            String author = scanner.nextLine();
            items.add(new Book(id, title, author));
            System.out.println("Book added successfully.");
        } else {
            System.out.print("Enter DVD ID:\n");
            String id = scanner.nextLine();
            System.out.print("Enter title:\n");
            String title = scanner.nextLine();
            System.out.print("Enter duration (minutes):\n");
            while (!scanner.hasNextInt()) {
                scanner.next(); // Clear invalid input
                System.out.print("Enter duration (minutes):\n");
            }
            int duration = scanner.nextInt();
            scanner.nextLine();
            items.add(new DVD(id, title, duration));
            System.out.println("DVD added successfully.");
        }
    }
    
    private void removeItem() {
        System.out.print("Enter item ID to remove:\n");
        String id = scanner.nextLine();
        if (items.removeIf(item -> item.getId().equals(id))) {
            System.out.println("Item removed successfully.");
        } else {
            System.out.println("Item not found.");
        }
    }
    
    private void listAllItems() {
        if (items.isEmpty()) {
            System.out.println("No items in the library.");
        } else {
            for (Item item : items) {
                System.out.println(item);
            }
        }
    }
    
    private void searchByTitle() {
        System.out.print("Enter title to search for:\n");
        String query = scanner.nextLine().toLowerCase();
        boolean found = false;
        for (Item item : items) {
            if (item.getTitle().toLowerCase().contains(query)) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found with that title.");
        }
    }
    
    private void saveToFile() {
        System.out.print("Enter filename to save:\n");
        String filename = scanner.nextLine();
        fileHandler.saveToFile(filename, items);
    }
    
    private void loadFromFile() {
        System.out.print("Enter filename to load:\n");
        String filename = scanner.nextLine();
        items = fileHandler.loadFromFile(filename);
    }
}
