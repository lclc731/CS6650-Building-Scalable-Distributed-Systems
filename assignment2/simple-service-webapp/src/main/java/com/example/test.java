package com.example;

import com.example.dal.ConnectionManager;
import com.example.dal.StepCountsDao;
import com.example.model.StepCounts;

import java.sql.SQLException;

/**
 * Created by ChangLiu on 10/25/18.
 */
public class test {
    public static void main(String[] args) throws SQLException {

        ConnectionManager connectionManager = new ConnectionManager();
        StepCountsDao stepCountsDao = StepCountsDao.getInstance();

        StepCounts stepCounts = new StepCounts(1, 1, 1, 1);
        StepCounts stepCounts1 = stepCountsDao.create(stepCounts);
        int i = 0;
    }

}
