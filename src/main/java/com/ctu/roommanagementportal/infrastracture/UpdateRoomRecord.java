package com.ctu.roommanagementportal.infrastracture;

import com.ctu.roommanagementportal.abstraction.Room;
import com.ctu.roommanagementportal.dbservices.SelectFromDB;
import com.ctu.roommanagementportal.model.RoomType;
import com.ctu.roommanagementportal.dbservices.InsertRecords;
import static com.ctu.roommanagementportal.infrastracture.AdminLogin.isAdmin;

import java.sql.SQLException;
import java.util.Scanner;

public class UpdateRoomRecord {

    public boolean execute() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        InsertRecords insertRecords = new InsertRecords();

        if (isAdmin()) {
            boolean continueUpdating = true;
            while (continueUpdating) {
                Room existingRoom = null;
                String roomName = null;
                String buildingLocation = null;

                // Loop to ensure valid room name and building location are provided
                while (existingRoom == null) {
                    buildingLocation = promptForBuildingLocation(scanner);
                    roomName = promptForRoomName(scanner);

                    // Check if the room exists
                    existingRoom = SelectFromDB.getRoomByNameAndLocation(roomName, buildingLocation);
                    if (existingRoom == null) {
                        System.out.println("\nRoom not found. Please enter a valid room name and building location.");
                    } else {
                        // Display room information
                        displayRoomInfo(existingRoom);
                    }
                }

                boolean updating = true;
                while (updating) {
                    // Prompt user for the field to update
                    int choice = promptForUpdateChoice(scanner);
                    boolean updateMade = handleUpdateChoice(scanner, insertRecords, roomName, buildingLocation, existingRoom);

                    // Ask if the user wants to update another field for the same room, regardless of whether an update was made
                    updating = askToUpdateAnotherField(scanner);

                    // If the user does not want to update another field, ask if they want to update another room
                    if (!updating) {
                        continueUpdating = askToUpdateAnotherRoom(scanner);
                        break;
                    }
                }
            }
        } else {
            System.out.println("Only admins are allowed to update the system.");
        }
//        scanner.close();

        System.out.println("    THANK YOU FOR USING THE ROOM UPDATE SYSTEM!!");
        return false;

    }

    // Method to display room information
    private static void displayRoomInfo(Room room) {
        System.out.println("--------------------------------------------------------------");
        System.out.println("|                Details of the Room You Entered             |");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("| %-25s | %-30s |%n", "Room Name", room.getRoomName());
        System.out.printf("| %-25s | %-30s |%n", "Building Location", room.getBuildingLocation());
        System.out.printf("| %-25s | %-30s |%n", "Capacity", room.getCapacity());
        System.out.printf("| %-25s | %-30s |%n", "Room Status", (room.getRoomStatus() ? "Available" : "Unavailable"));
        System.out.printf("| %-25s | %-30s |%n", "Maintenance Notes", room.getMaintenanceNotes());
        System.out.printf("| %-25s | %-30s |%n", "Projector Availability", (room.isHasProjector() ? "Yes" : "No"));
        System.out.printf("| %-25s | %-30s |%n", "Number of Chairs", room.getNumOfChairs());

        if (room instanceof RoomType.Classroom) {
            System.out.printf("| %-25s | %-30s |%n", "Whiteboard", (((RoomType.Classroom) room).isWhiteboard() ? "Yes" : "No"));
        } else if (room instanceof RoomType.CompLaboratory) {
            System.out.printf("| %-25s | %-30s |%n", "Number of Computers", ((RoomType.CompLaboratory) room).getNumOfComputers());
        } else if (room instanceof RoomType.Library) {
            System.out.printf("| %-25s | %-30s |%n", "Number of Desks", ((RoomType.Library) room).getNumOfDesks());
        } else if (room instanceof RoomType.Smartroom) {
            System.out.printf("| %-25s | %-30s |%n", "TV", (((RoomType.Smartroom) room).isTv() ? "Yes" : "No"));
            System.out.printf("| %-25s | %-30s |%n", "Internet Access", (((RoomType.Smartroom) room).isInternetAccess() ? "Yes" : "No"));
        }
        System.out.println("--------------------------------------------------------------");
    }


    // Method to prompt user for the building location
    private static String promptForBuildingLocation(Scanner scanner) {
        System.out.println("--------------------------------------------------");
        System.out.println("Building location of the room you want to update: ");
        System.out.println("--------------------------------------------------");
        System.out.println("A - Admin Building");
        System.out.println("T - College of Technology");
        System.out.println("E - College of Engineering");
        System.out.print("\nEnter your choice: ");
        return InputValidator.validateRoomBuilding(scanner);
    }

    // Method to prompt user for the room name
    private static String promptForRoomName(Scanner scanner) {
        System.out.print("\nEnter the name of the room you want to update: ");
        return InputValidator.validateNonEmptyStringInput(scanner);
    }

    // Method to prompt user for the field to update
    private static int promptForUpdateChoice(Scanner scanner) {
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("|                            Select the Field to Update                           |");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("| %-2s | %-74s |%n", "1", "Update room name");
        System.out.printf("| %-2s | %-74s |%n", "2", "Update capacity");
        System.out.printf("| %-2s | %-74s |%n", "3", "Update room Status");
        System.out.printf("| %-2s | %-74s |%n", "4", "Update building location");
        System.out.printf("| %-2s | %-74s |%n", "5", "Update maintenance notes");
        System.out.printf("| %-2s | %-74s |%n", "6", "Update projector availability");
        System.out.printf("| %-2s | %-74s |%n", "7", "Update number of chairs");
        System.out.printf("| %-2s | %-74s |%n", "8", "Room Type Specific Attributes (whiteboard, computers, desks, tv, internet)");
        System.out.printf("| %-2s | %-74s |%n", "9", "Exit current update");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.print("\nEnter the number of your choice: ");
        return 0;
    }

    // Method to handle user's update choice
    private static boolean handleUpdateChoice(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        boolean updateMade = false;
        int choice = -1;
        boolean validChoice = false;

        while (!validChoice) {
            String input = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 9) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
            }
        }

        switch (choice) {
            case 1:
                updateMade = updateRoomName(scanner, insertRecords, roomName, buildingLocation, existingRoom);
                break;
            case 2:
                updateMade = updateCapacity(scanner, insertRecords, roomName, buildingLocation, existingRoom);
                break;
            case 3:
                updateMade = updateRoomStatus(scanner, insertRecords, roomName, buildingLocation, existingRoom);
                break;
            case 4:
                updateMade = updateBuildingLocation(scanner, insertRecords, roomName, buildingLocation, existingRoom);
                break;
            case 5:
                updateMade = updateMaintenanceNotes(scanner, insertRecords, roomName, buildingLocation, existingRoom);
                break;
            case 6:
                updateMade = updateProjector(scanner, insertRecords, roomName, buildingLocation, existingRoom);
                break;
            case 7:
                updateMade = updateChairs(scanner, insertRecords, roomName, buildingLocation, existingRoom);
                break;
            case 8:
                updateMade = updateRoomTypeSpecificAttributes(scanner, insertRecords, roomName, buildingLocation, existingRoom);
                break;
            case 9:
                updateMade = false;
                break;
        }
        return updateMade;
    }


    private static boolean updateBuildingLocation(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        System.out.println("------------------------------");
        System.out.println("Enter new building location: ");
        System.out.println("------------------------------");
        System.out.println("    A - Admin Building");
        System.out.println("    T - College of Technology");
        System.out.println("    E - College of Engineering");
        System.out.print("\nEnter your choice: ");
        String newLocation = InputValidator.validateRoomBuilding(scanner);

        // Check if a room with the same name already exists in the new building location
        Room duplicateRoom = SelectFromDB.getRoomByNameAndLocation(roomName, newLocation);
        if (duplicateRoom != null && duplicateRoom.getBuildingLocation() != existingRoom.getBuildingLocation()) {
            System.out.println("A room with the same name already exists in the new building location. No update made.");
            return false;
        }

        // Check if the new location is the same as the existing room's location
        if (newLocation.equalsIgnoreCase(existingRoom.getBuildingLocation())) {
            System.out.println("New building location is the same as the current building location. No update made.");
            return false;
        }

        // Update the building location
        insertRecords.updateRoomField(roomName, buildingLocation, "buildingLocation", newLocation);
        System.out.println("Building location updated successfully.");
        return true;
    }


    private static boolean updateRoomName(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        System.out.print("\nEnter new room name: ");
        String newName = InputValidator.validateNonEmptyStringInput(scanner);

        // Check if a room with the same name already exists in the current building location
        Room duplicateRoom = SelectFromDB.getRoomByNameAndLocation(newName, buildingLocation);
        if (duplicateRoom != null) {
            System.out.println("A room with the same name already exists in this building location. NO UPDATE HAS BEEN MADE.");
            return false;
        }

        // Check if the new name is the same as the existing room's name
        if (newName.equalsIgnoreCase(existingRoom.getRoomName())) {
            System.out.println("New room name is the same as the current room name. No update made.");
            return false;
        }

        // Update the room name
        insertRecords.updateRoomField(roomName, buildingLocation, "roomName", newName);
        System.out.println("Room name updated successfully.");
        return true;
    }


    private static boolean updateCapacity(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        System.out.print("\nEnter new capacity: ");
        int newCapacity = InputValidator.validateIntegerInput(scanner);
        if (newCapacity != existingRoom.getCapacity()) {
            insertRecords.updateRoomField(roomName, buildingLocation, "capacity", newCapacity);
            System.out.println("Room capacity updated successfully.");
            return true;
        } else {
            System.out.println("New capacity is the same as the current capacity. No update made.");
            return false;
        }
    }

    private static boolean updateRoomStatus(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        System.out.print("\nIs the room available? (yes/no): ");
        boolean newStatus = InputValidator.validateBooleanInput(scanner);
        if (newStatus != existingRoom.getRoomStatus()) {
            insertRecords.updateRoomField(roomName, buildingLocation, "roomStatus", newStatus);
            System.out.println("Room status updated successfully.");
            return true;
        } else {
            System.out.println("New room status is the same as the current room status. No update made.");
            return false;
        }
    }

    private static boolean updateMaintenanceNotes(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        System.out.print("\nEnter new maintenance notes: ");
        String newNotes = InputValidator.validateNonEmptyStringInput(scanner);
        if (!newNotes.equals(existingRoom.getMaintenanceNotes())) {
            insertRecords.updateRoomField(roomName, buildingLocation, "maintenanceNotes", newNotes);
            System.out.println("Maintenance notes updated successfully.");
            return true;
        } else {
            System.out.println("New maintenance notes are the same as the current maintenance notes. No update made.");
            return false;
        }
    }

    private static boolean updateProjector(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        System.out.print("\nDoes it have a projector? (yes/no): ");
        boolean hasProjector = InputValidator.validateBooleanInput(scanner);
        if (hasProjector != existingRoom.isHasProjector()) {
            insertRecords.updateRoomField(roomName, buildingLocation, "hasProjector", hasProjector);
            System.out.println("Projector status updated successfully.");
            return true;
        } else {
            System.out.println("New projector status is the same as the current projector status. No update made.");
            return false;
        }
    }

    private static boolean updateChairs(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        System.out.print("\nEnter number of chairs: ");
        int numOfChairs = InputValidator.validateIntegerInput(scanner);
        if (numOfChairs != existingRoom.getNumOfChairs()) {
            insertRecords.updateRoomField(roomName, buildingLocation, "numOfChairs", numOfChairs);
            System.out.println("Number of chairs updated successfully.");
            return true;
        } else {
            System.out.println("New number of chairs is the same as the current number of chairs. No update made.");
            return false;
        }
    }

    private static boolean updateRoomTypeSpecificAttributes(Scanner scanner, InsertRecords insertRecords, String roomName, String buildingLocation, Room existingRoom) {
        boolean updateMade = true;

        if (existingRoom instanceof RoomType.Classroom) {
            System.out.print("\nDoes it have a whiteboard? (yes/no): ");
            boolean hasWhiteboard = InputValidator.validateBooleanInput(scanner);
            if (hasWhiteboard != ((RoomType.Classroom) existingRoom).isWhiteboard()) {
                insertRecords.updateRoomField(roomName, buildingLocation, "whiteBoard", hasWhiteboard);
                System.out.println("Whiteboard status updated successfully.");
                updateMade = false;
            } else {
                System.out.println("The current whiteboard status is already set to " + hasWhiteboard + ". No update needed.");
            }
        } else if (existingRoom instanceof RoomType.CompLaboratory) {
            System.out.print("\nEnter number of computers: ");
            int numOfComputers = InputValidator.validateIntegerInput(scanner);
            if (numOfComputers != ((RoomType.CompLaboratory) existingRoom).getNumOfComputers()) {
                insertRecords.updateRoomField(roomName, buildingLocation, "numOfComputers", numOfComputers);
                System.out.println("Number of computers updated successfully.");
                updateMade = false;
            } else {
                System.out.println("The current number of computers is already set to " + numOfComputers + ". No update needed.");
            }
        } else if (existingRoom instanceof RoomType.Library) {
            System.out.print("\nEnter number of desks: ");
            int numOfDesks = InputValidator.validateIntegerInput(scanner);
            if (numOfDesks != ((RoomType.Library) existingRoom).getNumOfDesks()) {
                insertRecords.updateRoomField(roomName, buildingLocation, "numOfDesks", numOfDesks);
                System.out.println("Number of desks updated successfully.");
                updateMade = false;
            } else {
                System.out.println("The current number of desks is already set to " + numOfDesks + ". No update needed.");
            }
        } else if (existingRoom instanceof RoomType.Smartroom) {
            System.out.print("\nDoes it have a TV? (yes/no): ");
            boolean hasTV = InputValidator.validateBooleanInput(scanner);
            if (hasTV != ((RoomType.Smartroom) existingRoom).isTv()) {
                insertRecords.updateRoomField(roomName, buildingLocation, "hasTV", hasTV);
                System.out.println("TV status updated successfully.");
                updateMade = false;
            } else {
                System.out.println("The current TV status is already set to " + hasTV + ". No update needed.");
            }

            System.out.print("\nDoes it have internet connection? (yes/no): ");
            boolean hasInternetAccess = InputValidator.validateBooleanInput(scanner);
            if (hasInternetAccess != ((RoomType.Smartroom) existingRoom).isInternetAccess()) {
                insertRecords.updateRoomField(roomName, buildingLocation, "hasInternetAccess", hasInternetAccess);
                System.out.println("Internet access updated successfully.");
                updateMade = false;
            } else {
                System.out.println("The current internet access status is already set to " + hasInternetAccess + ". No update needed.");
            }
        } else {
            System.out.println("Unknown room type.");
        }
        return updateMade;
    }

    // Method to prompt user if they want to update another field for the same room
    private static boolean askToUpdateAnotherField(Scanner scanner) {
        System.out.print("\nDo you want to update another field for the same room? (yes/no): ");
        return InputValidator.validateBooleanInput(scanner);
    }

    // Method to prompt user if they want to update another room
    private static boolean askToUpdateAnotherRoom(Scanner scanner) {
        System.out.print("\nDo you want to update another room? (yes/no): ");
        return InputValidator.validateBooleanInput(scanner);
    }
}