package com.example.dal;

import com.example.model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by ChangLiu on 10/25/18.
 */
public class UsersDao {
    protected ConnectionManager connectionManager;

    // Single pattern: instantiation is limited to one object.
    private static UsersDao instance = null;
    protected UsersDao() {
        connectionManager = new ConnectionManager();
    }
    public static UsersDao getInstance() {
        if(instance == null) {
            instance = new UsersDao();
        }
        return instance;
    }

    public Users create(Users Users) throws SQLException {

        String insertUser = "INSERT INTO Users(UserName) VALUES(?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertUser);
            insertStmt.setString(1, Users.getUserName());
            insertStmt.executeUpdate();
            return Users;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(insertStmt != null) {
                insertStmt.close();
            }
        }
    }

}
