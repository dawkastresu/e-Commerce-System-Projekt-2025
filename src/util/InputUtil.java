package util;

import java.util.Scanner;

public class InputUtil {

    public static double validateInputDouble(Scanner scanner, String content) {
     double choice = -1;
        while (choice == -1) {
            System.out.println(content);
            if (scanner.hasNextDouble()) {
                choice = scanner.nextDouble();
                if (choice <= 0) {
                    System.out.println("Wartość wejściowa nieprawidłowa. Spróbuj ponownie.");
                    choice = -1;
                }
            } else {
                System.out.println("Wartość wejściowa nieprawidłowa. Proszę podać liczbę całkowitą.");
                scanner.next(); // consume the invalid input
            }
        }
        return choice;
    }

    public static long validateInput(Scanner scanner, String content) {
        long choice = -1;
        while (choice == -1) {
            System.out.println(content);
            if (scanner.hasNextLong()) {
                choice = scanner.nextLong();
                if (choice <= 0) {
                    System.out.println("Wartość wejściowa nieprawidłowa. Spróbuj ponownie.");
                    choice = -1;
                }
            } else {
                System.out.println("Wartość wejściowa nieprawidłowa. Proszę podać liczbę całkowitą.");
                scanner.next(); // consume the invalid input
            }
        }
        return choice;
    }
}
