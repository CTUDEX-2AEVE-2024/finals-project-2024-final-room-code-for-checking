/**
 * Package declaration specifying the location of this project structure
 */
package com.ctu.roommanagementportal.abstraction;

/**
 *This code defines an abstract class named 'Room'.
 * An abstract class cannot be directly instantiated (you can't create object of this class directly).
 * It's meant to serve as a blueprint for subclasses that inherit methods.
 */
public abstract class Room {

    /**
     * Member variables (also called fields) to store information about a room.
     * -roomNumber: String to store the room identifier
     * -capacity: int to store the maximum number of occupants
     * -roomStatus: boolean to indicate if the room available (true) or occupied (false)
     * -buildingLocation: String to store the building where the room is located (consider moving this to subclasses if specific for each type)
     * -roomType: String to specify the type of room (e.g., Classroom, library, etc.)
     * -hasProjector: boolean to indicate if the room has a projector
     * These member variables are declared with 'protected' access modifier
     * This means they are accessible within the class itself, subclasses, and the package they are declared in
     */
    protected String roomName; // Encapsulation: Keep details hidden and access them through getters and setters
    protected int capacity;
    protected boolean roomStatus;
    protected String buildingLocation;
    protected String maintenanceNotes;
    protected String roomType;
    protected boolean hasProjector;
    protected int numOfChairs;

    // Constructor to initialize a 'Room' object
    // It takes several argument to set the initial values for the member variables
    public Room(String roomNumber, int capacity, boolean roomStatus, String buildingLocation, String maintenanceNotes,
                String roomType, boolean hasProjector, int numOfChairs) {
        this.roomName = roomNumber;
        this.capacity = capacity;
        this.roomStatus= roomStatus;
        this.buildingLocation=buildingLocation;
        this.maintenanceNotes = maintenanceNotes;
        this.hasProjector = hasProjector;
        this.roomType= roomType;
        this.numOfChairs = numOfChairs;
    }

    // Abstract method for displaying room information
    // Subclasses inheriting from 'Room' must implement this method to provide specific about their room type
    // Since this is an abstract method, it has nobody here in the parent class
    public abstract void displayRoomInfo(); // Abstraction

    // Getters and setters for encapsulation
    //These methods follow the convention of 'get' for retrieving values and 'set' for updating values
    //They provide controlled access to the member variables following principles of encapsulation
    public String getRoomName() {
        return roomName;
    }

    /**
     * Setter for the room number for the room
     * this maybe useful for future expansion or certain room management functionalities.
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Setter for the capacity for the room
     * this maybe useful for future expansion or certain room management functionalities.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean getRoomStatus() {
        return roomStatus;
    }

    /**
     * Setter for the room status for the room
     * this maybe useful for future expansion or certain room management functionalities.
     */
    public void setRoomStatus(boolean roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getBuildingLocation() {
        return buildingLocation;
    }

    /**
     * Setter for the building location for the room
     * this maybe useful for future expansion or certain room management functionalities.
     */
    public void setBuildingLocation(String buildingLocation) {
        this.buildingLocation = buildingLocation;
    }

    public String getMaintenanceNotes() {
        return maintenanceNotes;
    }

    /**
     * Setter for the maintenance notes for the room
     * this maybe useful for future expansion or certain room management functionalities.
     */
    public void setMaintenanceNotes(String maintenanceNotes) {
        this.maintenanceNotes = maintenanceNotes;
    }
    public String getRoomType() {
        return roomType;
    }

    /**
     * Setter for the room type for the room
     * this maybe useful for future expansion or certain room management functionalities.
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isHasProjector() {
        return hasProjector;
    }

    /**
     * Setter for the has projector for the room
     * this maybe useful for future expansion or certain room management functionalities.
     */
    public void setHasProjector(boolean hasProjector) {
        this.hasProjector = hasProjector;
    }

    public int getNumOfChairs(){
        return numOfChairs;
    }

    public void setNumOfChairs(int numOfChairs){
        this.numOfChairs=numOfChairs;
    }
}