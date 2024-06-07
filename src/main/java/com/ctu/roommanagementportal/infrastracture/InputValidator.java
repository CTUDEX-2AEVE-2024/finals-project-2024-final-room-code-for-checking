/**
 * Package declaration for the InputValidator class
 */

package com.ctu.roommanagementportal.infrastracture;

//Import statement

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.*;

/**
 * Represents a class for validating user input.
 */
public class InputValidator {

    private static final Set<String> ROOM_TYPES = new HashSet<>(Arrays.asList("CLASSROOM", "COMPUTER LABORATORY", "LIBRARY", "SMART ROOM"));
    private static final Map<String, String> NUMBER_TO_ROOM_TYPE = new HashMap<>();

    static {
        NUMBER_TO_ROOM_TYPE.put("1", "CLASSROOM");
        NUMBER_TO_ROOM_TYPE.put("2", "LABORATORY");
        NUMBER_TO_ROOM_TYPE.put("3", "LIBRARY");
        NUMBER_TO_ROOM_TYPE.put("4", "SMART ROOM");
    }
    public static String validateRoomTypeInput(Scanner scanner) {
        while (true) {
            // Read the entire line and trim it to remove any leading/trailing spaces
            String input = scanner.nextLine().trim().toUpperCase();

            // Check if the trimmed input is empty
            if (input.isEmpty()) {
                System.out.print("Invalid input. Please enter a valid room type: ");
                continue;
            }

            // Validate if the input is a valid room type or a valid number corresponding to a room type
            if (ROOM_TYPES.contains(input)) {
                return input; // Return the valid input if it's in the set
            } else if (NUMBER_TO_ROOM_TYPE.containsKey(input)) {
                return NUMBER_TO_ROOM_TYPE.get(input); // Return the corresponding room type for the number
            } else {
                System.out.print("Invalid input. Please enter a valid room type: ");
            }
        }
    }


    /**
     * Validates non-empty string input without spaces.
     * @param scanner The Scanner object to read user input.
     * @return The validated non-empty string input.
     */
    public static String validateNonEmptyStringInput(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.trim().isEmpty()) {
                System.out.print("Invalid input. Please enter a non-empty string:");
            } else {
                return input;
            }
        }
    }



    /**
     * Validates integer input
     * @param scanner The Scanner object to read user input.
     * @return The validated integer input.
     */
    public static int validateIntegerInput(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim(); // Read entire line and trim whitespace

            if (input.isEmpty()) { // Check if input is empty after trimming
                System.out.print("Invalid input. Please enter a non-empty integer: ");
                continue; // Ask for input again
            }

            try {
                // Attempt to parse the integer
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // Handle invalid integer input
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
    }


    /**
     * Validates boolean input.
     * @param scanner The Scanner object to read user input.
     * @return The validated boolean input.
     */
    public static boolean validateBooleanInput(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase(); // Read and normalize input

            if (input.isEmpty()) { // Check if the trimmed input is empty
                System.out.print("Invalid input. Please enter 'yes' or 'no': ");
            } else if (input.equals("YES")) { // Check for 'yes'
                return true;
            } else if (input.equals("NO")) { // Check for 'no'
                return false;
            } else {
                System.out.print("Invalid input. Please enter 'yes' or 'no': "); // Handle invalid values
            }
        }
    }

    /**
     * Validates room building name input.
     * @param scanner The Scanner object to read user input.
     * @return The validated building name.
     */
    public static String validateRoomBuilding(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();
            switch (input) {
                case "A":
                    return "Admin Building";
                case "T":
                    return "College of Technology";
                case "E":
                    return "College of Engineering";
                default:
                    System.out.print("Invalid input. Please enter A, T, or E: ");
            }
        }
    }
}
