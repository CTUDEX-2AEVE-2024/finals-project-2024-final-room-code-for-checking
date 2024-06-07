package com.ctu.roommanagementportal.dbservices;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ctu.roommanagementportal.abstraction.Room;
import com.ctu.roommanagementportal.model.RoomType;

public class SelectFromDB {

    private static final String QUERY_USER_BY_ROOMNAME = "SELECT * FROM ROOMRECORD WHERE roomName = ?";
    private static final String QUERY_USER_BY_ROOMTYPE = "SELECT * FROM ROOMRECORD WHERE roomType = ?";

    private static final String QUERY_USER_BY_PROJECTOR = "SELECT * FROM ROOMRECORD WHERE hasProjector = ? ";
    private static final String QUERY_USER_BY_WHITEBOARD = "SELECT * FROM ROOMRECORD WHERE whiteBoard = true";
    private static final String QUERY_USER_BY_CHAIRS = "SELECT * FROM ROOMRECORD WHERE numOfChairs > 0";
    private static final String QUERY_USER_BY_COMPUTERS = "SELECT * FROM ROOMRECORD WHERE numOfComputers > 0";
    private static final String QUERY_USER_BY_DESKS = "SELECT * FROM ROOMRECORD WHERE numOfDesks > 0";
    private static final String QUERY_USER_BY_TV = "SELECT * FROM ROOMRECORD WHERE hasTV = ?";
    private static final String QUERY_USER_BY_INTERNET_ACCESS = "SELECT * FROM ROOMRECORD WHERE hasInternetAccess = ?";
    private static final String GET_ROOM_SQL = "SELECT * FROM roomrecord WHERE roomName = ? AND buildingLocation = ?";


    public static Room getRoomByNameAndLocation(String roomName, String buildingLocation) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ROOM_SQL)) {

            preparedStatement.setString(1, roomName);
            preparedStatement.setString(2, buildingLocation);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the room data and return a Room object
                    String roomType = resultSet.getString("roomType");
                    int capacity = resultSet.getInt("capacity");
                    boolean roomStatus = resultSet.getBoolean("roomStatus");
                    String maintenanceNotes = resultSet.getString("maintenanceNotes");
                    boolean hasProjector = resultSet.getBoolean("hasProjector");
                    int numOfChairs = resultSet.getInt("numOfChairs");

                    switch (roomType) {
                        case "Classroom":
                            boolean hasWhiteboard = resultSet.getBoolean("whiteBoard");
                            return new RoomType.Classroom(roomName, capacity, roomStatus, buildingLocation, maintenanceNotes, hasProjector, numOfChairs, hasWhiteboard);
                        case "CompLaboratory":
                            int numOfComputers = resultSet.getInt("numOfComputers");
                            return new RoomType.CompLaboratory(roomName, capacity, roomStatus, buildingLocation, maintenanceNotes, hasProjector, numOfChairs, numOfComputers);
                        case "Library":
                            int numOfDesks = resultSet.getInt("numOfDesks");
                            return new RoomType.Library(roomName, capacity, roomStatus, buildingLocation, maintenanceNotes, hasProjector, numOfChairs, numOfDesks);
                        case "Smart room":
                            boolean hasTV = resultSet.getBoolean("hasTV");
                            boolean hasInternetAccess = resultSet.getBoolean("hasInternetAccess");
                            return new RoomType.Smartroom(roomName, capacity, roomStatus, buildingLocation, maintenanceNotes, hasProjector, numOfChairs, hasTV, hasInternetAccess);
                        default:
                            throw new IllegalArgumentException("Invalid room type.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Room> getRoomsWithProjector(boolean queryParam) {
        List<Room> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_PROJECTOR)) {
            preparedStatement.setBoolean(1, queryParam);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Determine the room type
                String roomType = resultSet.getString("roomType");

                // Based on the room type, instantiate the appropriate subclass of Room
                Room room;
                switch (roomType) {
                    case "Classroom":
                        room = new RoomType.Classroom(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getBoolean("whiteBoard")
                        );
                        break;
                    case "Smart room":
                        room = new RoomType.Smartroom(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getBoolean("hasTV"),
                                resultSet.getBoolean("hasInternetAccess"));

                        break;
                    case "Library":
                        room = new RoomType.Library(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getInt("numOfDesks"));
                        break;
                    case "CompLaboratory":
                        room = new RoomType.CompLaboratory(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getInt("numOfComputers"));
                        break;
                    default:
                        // Handle unknown room types
                        throw new IllegalArgumentException("Unknown room type: " + roomType);
                }

                rooms.add(room);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rooms;
    }

    public static List<Room> getRoomsWithChairs() {
        List<Room> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_CHAIRS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Determine the room type
                String roomType = resultSet.getString("roomType");

                // Based on the room type, instantiate the appropriate subclass of Room
                Room room;
                switch (roomType) {
                    case "Classroom":
                        room = new RoomType.Classroom(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getBoolean("whiteBoard")
                        );
                        break;
                    case "Smart room":
                        room = new RoomType.Smartroom(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getBoolean("hasTV"),
                                resultSet.getBoolean("hasInternetAccess"));

                        break;
                    case "Library":
                        room = new RoomType.Library(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getInt("numOfDesks"));
                        break;
                    case "CompLaboratory":
                        room = new RoomType.CompLaboratory(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getInt("numOfComputers"));
                        break;
                    default:
                        // Handle unknown room types
                        throw new IllegalArgumentException("Unknown room type: " + roomType);
                }

                rooms.add(room);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rooms;
    }

    public static List<Room> getRoomsByName(String queryParam) {
        List<Room> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_ROOMNAME)) {
            preparedStatement.setString(1, queryParam);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Determine the room type
                String roomType = resultSet.getString("roomType");

                // Based on the room type, instantiate the appropriate subclass of Room
                Room room;
                switch (roomType) {
                    case "Classroom":
                        room = new RoomType.Classroom(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getBoolean("whiteBoard")
                        );
                        break;
                    case "Smart room":
                        room = new RoomType.Smartroom(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getBoolean("hasTV"),
                                resultSet.getBoolean("hasInternetAccess"));

                        break;
                    case "Library":
                        room = new RoomType.Library(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getInt("numOfDesks"));
                        break;
                    case "CompLaboratory":
                        room = new RoomType.CompLaboratory(
                                resultSet.getString("roomName"),
                                resultSet.getInt("capacity"),
                                resultSet.getBoolean("roomStatus"),
                                resultSet.getString("buildingLocation"),
                                resultSet.getString("maintenanceNotes"),
                                resultSet.getBoolean("hasProjector"),
                                resultSet.getInt("numOfChairs"),
                                resultSet.getInt("numOfComputers"));
                        break;
                    default:
                        // Handle unknown room types
                        throw new IllegalArgumentException("Unknown room type: " + roomType);
                }

                rooms.add(room);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rooms;
    }

    public static List<RoomType.Classroom> getRoomsByType(String roomType) throws SQLException {
        List<RoomType.Classroom> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_ROOMTYPE)) {
            preparedStatement.setString(1, roomType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.Classroom classroom = new RoomType.Classroom(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfChairs"),
                        resultSet.getBoolean("whiteBoard"));
                rooms.add(classroom);
            }
        }
        return rooms;
    }


    public static List<RoomType.CompLaboratory> getCompLaboratoriesByType(String roomType) throws SQLException {
        List<RoomType.CompLaboratory> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_ROOMTYPE)) {
            preparedStatement.setString(1, roomType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.CompLaboratory laboratory = new RoomType.CompLaboratory(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfChairs"),
                        resultSet.getInt("numOfComputers"));
                rooms.add(laboratory);
            }
        }
        return rooms;
    }


    public static List<RoomType.Library> getLibrariesByType(String roomType) throws SQLException {
        List<RoomType.Library> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_ROOMTYPE)) {
            preparedStatement.setString(1, roomType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.Library library = new RoomType.Library(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfChairs"),
                        resultSet.getInt("numOfDesks"));
                rooms.add(library);
            }
        }
        return rooms;
    }

    public static List<RoomType.Smartroom> getSmartroomsByType(String roomType) throws SQLException {
        List<RoomType.Smartroom> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_ROOMTYPE)) {
            preparedStatement.setString(1, roomType);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.Smartroom smartroom = new RoomType.Smartroom(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfChairs"),
                        resultSet.getBoolean("hasTV"),
                        resultSet.getBoolean("hasInternetAccess"));
                ;
                rooms.add(smartroom);
            }
        }
        return rooms;
    }


    public static List<RoomType.Classroom> getRoomsByWhiteboard(boolean whiteBoard) throws SQLException {
        List<RoomType.Classroom> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_WHITEBOARD)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.Classroom classroom = new RoomType.Classroom(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfChairs"),
                        resultSet.getBoolean("whiteBoard"));
                rooms.add(classroom);
            }

        }
        return rooms;
    }
    // Method to retrieve rooms with computers
    public static List<RoomType.CompLaboratory> getRoomsByComputers() throws SQLException {
        List<RoomType.CompLaboratory> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_COMPUTERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.CompLaboratory laboratory = new RoomType.CompLaboratory(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfComputers"),
                        resultSet.getInt("numOfChairs"));
                rooms.add(laboratory);
            }
        }
        return rooms;
    }

    // Method to retrieve rooms with desks
    public static List<RoomType.Library> getRoomsByDesks() throws SQLException {
        List<RoomType.Library> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_DESKS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.Library library = new RoomType.Library(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfChairs"),
                        resultSet.getInt("numOfDesks"));
                rooms.add(library);
            }
        }
        return rooms;
    }

    // Method to retrieve rooms with TV
    public static List<RoomType.Smartroom> getRoomsByTV(boolean hasTV) throws SQLException {
        List<RoomType.Smartroom> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_TV)) {
            preparedStatement.setBoolean(1, hasTV);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.Smartroom smartroom = new RoomType.Smartroom(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfChairs"),
                        resultSet.getBoolean("hasTV"),
                        resultSet.getBoolean("hasInternetAccess"));

                rooms.add(smartroom);
            }
        }
        return rooms;
    }

    // Method to retrieve rooms with internet access
    public static List<RoomType.Smartroom> getRoomsByInternetAccess(boolean hasInternetAccess) throws SQLException {
        List<RoomType.Smartroom> rooms = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true",
                "root",
                "admin123$");

             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_USER_BY_INTERNET_ACCESS)) {
            preparedStatement.setBoolean(1, hasInternetAccess);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RoomType.Smartroom smartroom = new RoomType.Smartroom(
                        resultSet.getString("roomName"),
                        resultSet.getInt("capacity"),
                        resultSet.getBoolean("roomStatus"),
                        resultSet.getString("buildingLocation"),
                        resultSet.getString("maintenanceNotes"),
                        resultSet.getBoolean("hasProjector"),
                        resultSet.getInt("numOfChairs"),
                        resultSet.getBoolean("hasTV"),
                        resultSet.getBoolean("hasInternetAccess"));

                rooms.add(smartroom);
            }
        }
        return rooms;
    }

}
