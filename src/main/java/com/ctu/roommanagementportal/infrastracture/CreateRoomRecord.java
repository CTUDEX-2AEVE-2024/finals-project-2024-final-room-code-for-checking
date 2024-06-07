/**
 * This class allows users to create room records and save them to a database.
 * It provides methods for gathering information about different types of rooms such as classrooms,
 * computer laboratories, libraries, and smart rooms.
 */
package com.ctu.roommanagementportal.infrastracture;

import com.ctu.roommanagementportal.abstraction.Room;
import com.ctu.roommanagementportal.model.RoomType;
import com.ctu.roommanagementportal.dbservices.InsertRecords;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * The main class that contains the main method to run the program.
 * It prompts the user to select a room type, gathers information about the room,
 * creates a room object based on the user's input, and saves the room record to the database.
 */
public class CreateRoomRecord {

    /**
     * Method to create a new room record.
     * @throws SQLException if a database access error occurs.
     */
    public boolean execute() throws SQLException {

        // Uncomment this line if you need to create the necessary tables in the database
        // CreateTables createTables = new CreateTables();

        // Scanner object to read user input from the console.
        Scanner scanner = new Scanner(System.in);

        //Initialize a boolean variable to control the while loop
        boolean exitProgram = false;

        // Loop until the user decides not to create another room record.
        while (!exitProgram) {

            // Prompting the user to enter the building location.
            System.out.println("\n------------------------");
            System.out.println("Enter building location:");
            System.out.println("------------------------");
            System.out.println("A - Admin Building");
            System.out.println("T - College of Technology");
            System.out.println("E - College of Engineering");
            System.out.print("\nEnter your choice: ");

            // Validate and read the building location input.
            String buildingLocation = InputValidator.validateRoomBuilding(scanner);

            // Prompting the user to select the type of room.
            System.out.println("\n------------------");
            System.out.println("Select a room type");
            System.out.println("------------------");
            System.out.println("1. Classroom");
            System.out.println("2. Computer Laboratory");
            System.out.println("3. Library");
            System.out.println("4. Smart room");
            System.out.print("\nEnter room type (1 - 4 or room type name): ");
            // Validate and read the room type input.
            String roomType = InputValidator.validateRoomTypeInput(scanner);

            // Create a new room object based on user input.
            Room newRoom = createRoom(scanner, roomType, buildingLocation);

            //Create an InsertRecords object to handle database insertion.
            InsertRecords insertRecords = new InsertRecords();

            // Attempt to insert the room record into the database.
            boolean success = insertRecords.insertRoomRecord(newRoom);
            if (success) {
                // Notify the user if the room record was successfully inserted.
                System.out.println("Room record successfully inserted.");
            } else {
                // Notify the user if a room with the same name and location already exists.
                System.out.println("A room with the same name and location already exists. Please try again with different details.");
            }
            // Check if the user wants to create another room record.
            exitProgram = !promptForAnotherCreation(scanner);
        }
        System.out.println("    THANK YOU FOR USING THE ROOM CREATION SYSTEM!!");
        return false;
    }

    /**
     * Prompts the user if they want to create another room record.
     * @param scanner The Scanner object to read user input.
     * @return true if the user wants to create another room record, false otherwise.
     */
    public static boolean promptForAnotherCreation(Scanner scanner) {
        while (true) {
            System.out.print("\nDo you want to create another room record? (yes/no): ");
            String createAnother = scanner.nextLine().trim().toLowerCase();

            if (createAnother.equals("yes")) {
                return true; // Continue with another record creation
            } else if (createAnother.equals("no")) {
                return false; // Terminate the program
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }

    /**
     * Creates a room object based on user input.
     * @param scanner The Scanner object to read user input.
     * @param roomType The type of room to create.
     * @param buildingLocation The location of the building.
     * @return A Room object representing the created room.
     */
    private static Room createRoom(Scanner scanner, String roomType, String buildingLocation) {
        // Prompt and validate the room name input.
        System.out.print("\nEnter room name: ");
        String roomName = InputValidator.validateNonEmptyStringInput(scanner);

        // Prompt and validate the room capacity input.
        System.out.print("\nEnter room capacity: ");
        int capacity = InputValidator.validateIntegerInput(scanner);

        // Prompt and validate the number of chairs input.
        System.out.print("\nEnter number of chairs: ");
        int numOfChairs = InputValidator.validateIntegerInput(scanner);

        // Prompt and validate the room availability input.
        System.out.print("\nIs the room available? (yes/no): ");
        boolean roomStatus = InputValidator.validateBooleanInput(scanner);

        // Prompt and validate the projector availability input.
        System.out.print("\nDoes it have a projector? (yes/no): ");
        boolean hasProjector = InputValidator.validateBooleanInput(scanner);

        // Prompt and validate the maintenance notes input.
        System.out.print("\nProvide maintenance notes: ");
        String maintenanceNotes = InputValidator.validateNonEmptyStringInput(scanner).trim();

        // Create and return the appropriate Room object based on the room type.
        switch (roomType) {
            case "CLASSROOM":
                return createClassroom(scanner, roomName, capacity, roomStatus, hasProjector, buildingLocation, maintenanceNotes, numOfChairs);
            case "LABORATORY":
                return createLaboratory(scanner, roomName, capacity, roomStatus, hasProjector, buildingLocation, maintenanceNotes, numOfChairs);
            case "LIBRARY":
                return createLibrary(scanner, roomName, capacity, roomStatus, hasProjector, buildingLocation, maintenanceNotes, numOfChairs);
            case "SMART ROOM":
                return createSmartroom(scanner, roomName, capacity, roomStatus, hasProjector, buildingLocation, maintenanceNotes, numOfChairs);
            default:
                throw new IllegalArgumentException("Invalid room type.");
        }
    }

    /**
     * Creates a Classroom object based on user input.
     * @param scanner The Scanner object to read user input.
     * @param roomName The name of the room.
     * @param capacity The capacity of the room.
     * @param roomStatus The availability status of the room.
     * @param hasProjector The projector availability status.
     * @param buildingLocation The location of the building.
     * @param maintenanceNotes The maintenance notes for the room.
     * @param numOfChairs The number of chairs in the room.
     * @return A Classroom object representing the created classroom.
     */
    private static Room createClassroom(Scanner scanner, String roomName, int capacity, boolean roomStatus, boolean hasProjector,
                                        String buildingLocation, String maintenanceNotes, int numOfChairs) {
        // Prompt and validate the whiteboard availability input.
        System.out.print("\nDoes it have a whiteboard? (yes/no): ");
        boolean hasWhiteboard = InputValidator.validateBooleanInput(scanner);

        // Return a new Classroom object.
        return new RoomType.Classroom(roomName, capacity, roomStatus, buildingLocation, maintenanceNotes, hasProjector, numOfChairs, hasWhiteboard);
    }

    /**
     * Creates a CompLaboratory object based on user input.
     * @param scanner The Scanner object to read user input.
     * @param roomName The name of the room.
     * @param capacity The capacity of the room.
     * @param roomStatus The availability status of the room.
     * @param hasProjector The projector availability status.
     * @param buildingLocation The location of the building.
     * @param maintenanceNotes The maintenance notes for the room.
     * @param numOfChairs The number of chairs in the room.
     * @return A CompLaboratory object representing the created computer laboratory.
     */
    private static Room createLaboratory(Scanner scanner, String roomName, int capacity, boolean roomStatus, boolean hasProjector,
                                         String buildingLocation, String maintenanceNotes, int numOfChairs) {
        // Prompt and validate the number of computers input.
        System.out.print("\nEnter number of computers: ");
        int numOfComputers = InputValidator.validateIntegerInput(scanner);

        // Return a new CompLaboratory object.
        return new RoomType.CompLaboratory(roomName, capacity, roomStatus, buildingLocation, maintenanceNotes, hasProjector, numOfChairs, numOfComputers);
    }

    /**
     * Creates a Library object based on user input.
     * @param scanner The Scanner object to read user input.
     * @param roomName The name of the room.
     * @param capacity The capacity of the room.
     * @param roomStatus The availability status of the room.
     * @param hasProjector The projector availability status.
     * @param buildingLocation The location of the building.
     * @param maintenanceNotes The maintenance notes for the room.
     * @param numOfChairs The number of chairs in the room.
     * @return A Library object representing the created library.
     */
    private static Room createLibrary(Scanner scanner, String roomName, int capacity, boolean roomStatus, boolean hasProjector,
                                      String buildingLocation, String maintenanceNotes, int numOfChairs) {
        // Prompt and validate the number of desks input.
        System.out.print("\nEnter number of desks: ");
        int numOfDesks = InputValidator.validateIntegerInput(scanner);

        // Return a new Library object.
        return new RoomType.Library(roomName, capacity, roomStatus, buildingLocation, maintenanceNotes, hasProjector, numOfChairs, numOfDesks);
    }

    /**
     * Creates a Smartroom object based on user input.
     * @param scanner The Scanner object to read user input.
     * @param roomName The name of the room.
     * @param capacity The capacity of the room.
     * @param roomStatus The availability status of the room.
     * @param hasProjector The projector availability status.
     * @param buildingLocation The location of the building.
     * @param maintenanceNotes The maintenance notes for the room.
     * @param numOfChairs The number of chairs in the room.
     * @return A Smartroom object representing the created smart room.
     */
    private static Room createSmartroom(Scanner scanner, String roomName, int capacity, boolean roomStatus, boolean hasProjector,
                                        String buildingLocation, String maintenanceNotes, int numOfChairs) {
        // Prompt and validate the TV availability input.
        System.out.print("\nDoes it have a TV? (yes/no): ");
        boolean hasTV = InputValidator.validateBooleanInput(scanner);

        // Prompt and validate the internet access input.
        System.out.print("\nDoes it have internet connection? (yes/no): ");
        boolean hasInternetAccess = InputValidator.validateBooleanInput(scanner);

        // Return a new Smartroom object.
        return new RoomType.Smartroom(roomName, capacity, roomStatus, buildingLocation, maintenanceNotes, hasProjector, numOfChairs, hasTV, hasInternetAccess);
    }
}
