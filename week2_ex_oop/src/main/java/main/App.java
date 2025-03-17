package main;
import java.util.Scanner;
/**
 * App class
 */

public class App {
    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hedgehog hedgehog = new Hedgehog("Pikseli", 5); // Create a default hedgehog instance

        while (true) {
            System.out.println("1) Make hedgehog talk, 2) Create new hedgehog, 3) Make hedgehog run, 0) Quit");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("What does hedgehog say?");
                    String input = scanner.nextLine();
                    hedgehog.speak(input);
                    break;

                case "2":
                    System.out.println("What is the name of the hedgehog:");
                    String newName = scanner.nextLine();

                    System.out.println("What is the age of the hedgehog:");
                    try {
                        int newAge = Integer.parseInt(scanner.nextLine());
                        if (newAge < 0) {
                            System.out.println("Wrong input value");
                        } else {
                            hedgehog = new Hedgehog(newName, newAge);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Wrong input value");
                    }
                    break;

                case "3":
                    System.out.println("How many laps?");
                    try {
                        int laps = Integer.parseInt(scanner.nextLine());
                        hedgehog.run(laps);
                    } catch (NumberFormatException e) {
                        System.out.println("Wrong input value");
                    }
                    break;

                case "0":
                    System.out.println("Thank you for using the program.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Wrong input value");
            }
        }
    }
}