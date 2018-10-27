package com.example;

import com.example.dal.ConnectionManager;
import com.example.dal.StepCountsDao;
import com.example.dal.UtilDao;
import com.example.model.StepCounts;

import java.sql.SQLException;

/**
 * Created by ChangLiu on 10/25/18.
 */
public class test {
    public static void main(String[] args) throws SQLException {

        ConnectionManager connectionManager = new ConnectionManager();
        StepCountsDao stepCountsDao = StepCountsDao.getInstance();
        UtilDao utilDao = UtilDao.getInstance();

        StepCounts stepCounts = new StepCounts(1, 1, 1, 1);
        StepCounts stepCounts1 = stepCountsDao.create(stepCounts);
        stepCountsDao.create(new StepCounts(1, 2, 1, 2));
        int count = stepCountsDao.getStepCountByDay(1, 1);

        int count1 = stepCountsDao.getStepCountByDay(1, 2);
        int count2 = stepCountsDao.getStepCountCurrent(1);

        utilDao.cleanTable();
        int i = 0;

    }

}
