package main;

import java.util.Scanner;

class Hedgehog {
    String name;
    int age;

    // Default constructor
    Hedgehog() {
        name = "Pikseli";
        age = 5;
    }

    // Constructor to create a new hedgehog
    Hedgehog(String newName, int newAge) {
        name = newName;
        age = newAge;
    }

    // Speak method
    void speak(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("I am " + name + " and my age is " + age + ", but could you still give me input values?");
        } else {
            System.out.println(name + ": " + input);
        }
    }

    // Run method
    void run(int laps) {
        if (laps <= 0) {
            System.out.println("Wrong input value. Laps must be greater than 0.");
        } else {
            for (int i = 0; i < laps; i++) {
                System.out.println(name + " runs really fast!");
            }
        }
    }
}

public class HedgehogProgram {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create a default hedgehog
        Hedgehog hedgehog = new Hedgehog();

        while (true) {
            // Show menu options
            System.out.print("\n--- Hedgehog Menu ---\n" +
                             "1. Speak\n" +
                             "2. Run\n" +
                             "3. Create new hedgehog\n" +
                             "4. Show info\n" +
                             "5. Exit\n" +
                             "Choose an option: ");

            // Check if input is an integer
            if (!scanner.hasNextInt()) {
                System.out.println("Wrong input value. Please enter a number between 1 and 5.");
                scanner.next(); // Consume invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1: // Speak feature
                    System.out.print("What should the hedgehog say? ");
                    String input = scanner.nextLine();
                    hedgehog.speak(input);
                    break;

                case 2: // Run feature
                    System.out.print("How many laps? ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Wrong input value. Laps must be an integer.");
                        scanner.next(); // Consume invalid input
                        break;
                    }
                    int laps = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    hedgehog.run(laps);
                    break;

                case 3: // Create new hedgehog
                    System.out.print("Enter new hedgehog name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new hedgehog age: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Wrong input value. Age must be a non-negative integer.");
                        scanner.next(); // Consume invalid input
                        break;
                    }
                    int newAge = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    if (newAge < 0) {
                        System.out.println("Wrong input value. Age cannot be negative.");
                    } else {
                        hedgehog = new Hedgehog(newName, newAge);
                        System.out.println("New hedgehog created!");
                    }
                    break;

                case 4: // Show hedgehog info
                    System.out.println("Hedgehog name: " + hedgehog.name + ", Age: " + hedgehog.age);
                    break;

                case 5: // Exit the program
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;

                default: // Handle invalid choices
                    System.out.println("Wrong input value. Please choose a valid option between 1 and 5.");
            }
        }
    }
}
