package com.example.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by ChangLiu on 10/27/18.
 */
public class UtilDao {
    protected ConnectionManager connectionManager;

    // Single pattern: instantiation is limited to one object.
    private static UtilDao instance = null;
    protected UtilDao() {
        connectionManager = new ConnectionManager();
    }
    public static UtilDao getInstance() {
        if(instance == null) {
            instance = new UtilDao();
        }
        return instance;
    }

    /**
     * Create a step count record
     */
    public void cleanTable() throws SQLException {
        String deleteStepCounts = "TRUNCATE TABLE StepCounts;";
        Connection connection = null;
        PreparedStatement deleteStmt = null;
        try {
            connection = connectionManager.getConnection();
            deleteStmt = connection.prepareStatement(deleteStepCounts);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(deleteStmt != null) {
                deleteStmt.close();
            }
        }
    }
}
