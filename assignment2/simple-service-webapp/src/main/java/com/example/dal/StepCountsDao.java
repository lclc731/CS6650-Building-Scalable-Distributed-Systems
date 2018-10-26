package com.example.dal;

import com.example.model.StepCounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by ChangLiu on 10/26/18.
 */
public class StepCountsDao {
    protected ConnectionManager connectionManager;

    // Single pattern: instantiation is limited to one object.
    private static StepCountsDao instance = null;
    protected StepCountsDao() {
        connectionManager = new ConnectionManager();
    }
    public static StepCountsDao getInstance() {
        if(instance == null) {
            instance = new StepCountsDao();
        }
        return instance;
    }

    public StepCounts create(StepCounts stepCounts) throws SQLException {

        String insertStepCounts = "INSERT INTO StepCounts(UserId, Day, TimeInterval, StepCount) VALUES(?, ?, ?, ?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertStepCounts);
            insertStmt.setInt(1, stepCounts.getUserId());
            insertStmt.setInt(2, stepCounts.getDay());
            insertStmt.setInt(3, stepCounts.getTimeInterval());
            insertStmt.setInt(4, stepCounts.getStepCount());
            insertStmt.executeUpdate();
            return stepCounts;
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
