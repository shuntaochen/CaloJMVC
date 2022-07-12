package org.caloch.utils;

import com.mysql.jdbc.ResultSet;

import java.sql.*;

public class MysqlHelper {


    private Connection conn;

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() throws SQLException {
        conn.rollback();
    }

    public void connect() {
        String dbURL = "jdbc:mysql://localhost:3306/sampledb";
        String username = "root";
        String password = "secret";

        try {

            Connection conne = DriverManager.getConnection(dbURL, username, password);
            conne.setAutoCommit(false);//open transaction
            conn = conne;
            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void select() throws SQLException {//get list,
        String sql = "SELECT * FROM Users";

        Statement statement = conn.createStatement();
        ResultSet result = (ResultSet) statement.executeQuery(sql);

        int count = 0;

        while (result.next()) {
            String name = result.getString(2);
            String pass = result.getString(3);
            String fullname = result.getString("fullname");
            String email = result.getString("email");

            String output = "User #%d: %s - %s - %s - %s";
            System.out.println(String.format(output, ++count, name, pass, fullname, email));
        }
    }

    public void insert() throws SQLException {//insert
        String sql = "INSERT INTO Users (username, password, fullname, email) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "bill");
        statement.setString(2, "secretpass");
        statement.setString(3, "Bill Gates");
        statement.setString(4, "bill.gates@microsoft.com");

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }
    }

    public void update() throws SQLException {//update

        String sql = "UPDATE Users SET password=?, fullname=?, email=? WHERE username=?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "123456789");
        statement.setString(2, "William Henry Bill Gates");
        statement.setString(3, "bill.gates@microsoft.com");
        statement.setString(4, "bill");

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("An existing user was updated successfully!");
        }
    }

    public void delete() throws SQLException {//delete
        String sql = "DELETE FROM Users WHERE username=?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "bill");

        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("A user was deleted successfully!");
        }
    }

}
