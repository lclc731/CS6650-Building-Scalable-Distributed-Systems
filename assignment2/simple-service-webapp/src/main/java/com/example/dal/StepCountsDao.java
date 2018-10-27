package com.example.dal;

import com.example.model.StepCounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    /**
     * Create a step count record
     */
    public StepCounts create(StepCounts stepCounts) throws SQLException {

        String insertStepCounts = "INSERT INTO StepCounts(UserId, DayId, TimeInterval, StepCount) VALUES(?, ?, ?, ?);";
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(insertStepCounts);
            insertStmt.setInt(1, stepCounts.getUserId());
            insertStmt.setInt(2, stepCounts.getDayId());
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

    /**
     * Get the all the step count of a certain day
     */
    public int getStepCountByDay(int dayId) throws SQLException {
        String selectStepCounts = "SELECT StepCount FROM StepCounts WHERE DayId=?;";

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        int sum = 0;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectStepCounts);
            selectStmt.setInt(1, dayId);
            results = selectStmt.executeQuery();
            while(results.next()) {
                sum += results.getInt("StepCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(selectStmt != null) {
                selectStmt.close();
            }
            if(results != null) {
                results.close();
            }
        }
        return sum;
    }

    /**
     * Get the all the step count of a certain day
     */
    public int getStepCountCurrent() throws SQLException {
        String selectStepCounts = "SELECT DayId " +
                "FROM StepCounts " +
                "ORDER BY DayId " +
                "DESC " +
                "LIMIT 1;";

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        int sum = 0;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectStepCounts);
            results = selectStmt.executeQuery();
            while(results.next()) {
                sum = results.getInt("DayId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(selectStmt != null) {
                selectStmt.close();
            }
            if(results != null) {
                results.close();
            }
        }
        return getStepCountByDay(sum);
    }
}
