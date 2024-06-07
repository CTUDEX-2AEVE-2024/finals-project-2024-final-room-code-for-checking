package com.ctu.roommanagementportal.infrastracture;

import java.util.Scanner;
import java.sql.SQLException;


public class Main {
    private static boolean exit = false;

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        CreateRoomRecord createRoomRecord = new CreateRoomRecord();
        SearchRoomRecord searchRoomRecord = new SearchRoomRecord();
        UpdateRoomRecord updateRoomRecord = new UpdateRoomRecord();

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("\n|====================================================|");
            System.out.println("|        Welcome to the Room Management System       |");
            System.out.println("|====================================================|");
            System.out.println("|    Please choose an option from the menu below:    |");
            System.out.println("|====================================================|");
            System.out.println("| 1. Create a New Room Record                        |");
            System.out.println("| 2. Search for an Existing Room Record              |");
            System.out.println("| 3. Update an Existing Room Record                  |");
            System.out.println("| 4. Exit the System                                 |");
            System.out.println("|====================================================|");

            int choice = -1;
            boolean validChoice = false;

            while (!validChoice) {
                System.out.print("\nPlease enter your choice (1-4): ");
                String input = scanner.nextLine().trim();
                try {
                    choice = Integer.parseInt(input);
                    if (choice >= 1 && choice <= 4) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                }
            }

            switch (choice) {
                case 1:
                    System.out.println("\n****************************************");
                    System.out.println("           Room Creation System          ");
                    System.out.println("****************************************");
                    createRoomRecord.execute(); // Execute the operation
                    break;
                case 2:
                    System.out.println("\n****************************************");
                    System.out.println("           Room Search System            ");
                    System.out.println("****************************************");
                    searchRoomRecord.execute(); // Execute the operation
                    break;
                case 3:
                    System.out.println("\n****************************************");
                    System.out.println("           Room Update System            ");
                    System.out.println("****************************************");
                    updateRoomRecord.execute(); // Execute the operation
                    break;
                case 4:
                    System.out.println("\n************************************************");
                    System.out.println("  Exiting the Room Management System. Goodbye!  ");
                    System.out.println("************************************************");
                    continueRunning = false;
                    break;
            }

            // Ask if the user wants to perform another action if the choice is not to exit
            if (continueRunning) {
                promptForAnotherAction(scanner);
                if (exit) {
                    continueRunning = false;
                }
            }
        }

        scanner.close();
        System.exit(0);
    }

    public static void promptForAnotherAction(Scanner scanner) {
        boolean validInput = false;

        // Loop to ensure valid input is received
        while (!validInput) {
            System.out.print("\nDo you want to perform another action (CREATE, SEARCH , UPDATE) ? (yes/no): ");
            String choice = scanner.nextLine().toLowerCase().trim();
            if (choice.equals("yes")) {
                // Exit the loop and continue
                validInput = true;
            } else if (choice.equals("no")) {
                validInput = true;
                exit = true;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }
}
