package com.ctu.roommanagementportal.infrastracture;

import javax.swing.*;
import java.sql.*;

public class AdminLogin {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/roomportaldb?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin123$";

    public static void main(String[] args) {
        // Initialize the admin credentials if not already present
        initializeAdminCredentials();

        // Create new admin credentials (uncomment the line below to use this functionality)
       // createAdminCredentials();
    }

    private static void initializeAdminCredentials() {
        String checkQuery = "SELECT COUNT(*) FROM admininfo";
        String insertQuery = "INSERT INTO admininfo (email, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, "admin@example.com");
                    pstmt.setString(2, "ctu123$");  // For security, hash the password
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createAdminCredentials() {
        JFrame parentFrame = new JFrame();
        parentFrame.setAlwaysOnTop(true);

        String email = JOptionPane.showInputDialog(parentFrame, "Enter new admin email:", "Create Admin", JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.isEmpty()) {
            showMessage(parentFrame, "Admin email cannot be empty.", "Error");
            return;
        }

        JPasswordField passwordField = new JPasswordField();
        Object[] obj = {"Enter admin password:", passwordField};
        Object[] passwordOptions = {"OK", "Cancel"};
        int passwordResponse = JOptionPane.showOptionDialog(parentFrame, obj, "Create Admin",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, passwordOptions, passwordOptions[0]);
        if (passwordResponse == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            if (password.isEmpty()) {
                showMessage(parentFrame, "Password cannot be empty.", "Error");
                return;
            }
            storeAdminCredentials(email, password);
        } else {
            showMessage(parentFrame, "Admin creation cancelled.", "Warning");
        }
    }

    private static void storeAdminCredentials(String email, String password) {
        String insertQuery = "INSERT INTO admininfo (email, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);  // For security, hash the password
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showMessage(null, "New admin credentials have been created successfully.", "Success");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            showMessage(null, "This email is already registered as an admin.", "Error");
        } catch (SQLException e) {
            e.printStackTrace();
            showMessage(null, "Failed to create admin credentials.", "Error");
        }
    }

    public static boolean isAdmin() {
        JFrame parentFrame = new JFrame();
        parentFrame.setAlwaysOnTop(true);

        showMessage(parentFrame, "This interface is used to update room information.\nOnly admins are allowed to perform updates.", "Information");

        int response = showAdminQuestion(parentFrame, "Are you an admin?");
        if (response == JOptionPane.YES_OPTION) {
            String email = requestAdminEmail(parentFrame);
            if (email != null && validateAdminEmail(email)) {
                char[] passwordArray = requestAdminPassword(parentFrame);
                if (passwordArray != null) {
                    String password = new String(passwordArray);
                    if (validateAdminPassword(email, password)) {
                        showMessage(parentFrame, "Login successful, you can now update the system.", "Success");
                        return true;
                    } else {
                        showMessage(parentFrame, "Incorrect admin password.", "Error");
                    }
                } else {
                    showMessage(parentFrame, "Admin login cancelled.", "Warning");
                }
            } else {
                showMessage(parentFrame, "Invalid admin email.", "Error");
            }
        } else {
            showMessage(parentFrame, "Only admins are allowed to update the system.", "Warning");
        }
        return false;
    }

    private static void showMessage(JFrame parentFrame, String message, String title) {
        JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private static int showAdminQuestion(JFrame parentFrame, String question) {
        JLabel adminLabel = new JLabel(question);
        String[] options = {"Yes", "No"};
        return JOptionPane.showOptionDialog(parentFrame, adminLabel, "Admin Check",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
    }

    private static String requestAdminEmail(JFrame parentFrame) {
        return JOptionPane.showInputDialog(parentFrame, "Enter admin email:", "Admin Login", JOptionPane.QUESTION_MESSAGE);
    }

    private static boolean validateAdminEmail(String email) {
        String query = "SELECT COUNT(*) FROM admininfo WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static char[] requestAdminPassword(JFrame parentFrame) {
        JPasswordField passwordField = new JPasswordField();
        Object[] obj = {"Enter admin password:", passwordField};
        Object[] passwordOptions = {"OK", "Cancel"};
        int passwordResponse = JOptionPane.showOptionDialog(parentFrame, obj, "Admin Login",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, passwordOptions, passwordOptions[0]);
        if (passwordResponse == JOptionPane.OK_OPTION) {
            return passwordField.getPassword();
        }
        return null;
    }

    private static boolean validateAdminPassword(String email, String password) {
        String query = "SELECT password FROM admininfo WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(password); // For security, use hashed passwords in real applications
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
