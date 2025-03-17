package main;

public class Hedgehog {
    private String name;
    private int age;

    // Constructor with parameters
    public Hedgehog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Speak method
    public void speak(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("I am " + name + " and my age is " + age + ", but could you still give me input values?");
        } else {
            System.out.println(name + ": " + input);
        }
    }

    // Run method
    public void run(int laps) {
        if (laps <= 0) {
            System.out.println("Wrong input value");
        } else {
            for (int i = 0; i < laps; i++) {
                System.out.println(name + " runs really fast!");
            }
        }
    }

    // Main method to test the class
    public static void main(String[] args) {
        // Create a Hedgehog object
        Hedgehog hedgehog = new Hedgehog("Sonic", 3);

        // Test speak method
        hedgehog.speak(null);
        hedgehog.speak("Hello there!");

        // Test run method
        hedgehog.run(3);
        hedgehog.run(0);
    }
}
