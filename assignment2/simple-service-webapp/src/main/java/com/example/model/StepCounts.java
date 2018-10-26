package com.example.model;

/**
 * Created by ChangLiu on 10/26/18.
 */
public class StepCounts {
    protected int userId;
    protected int day;
    protected int timeInterval;
    protected int stepCount;

    public StepCounts(int userId, int day, int timeInterval, int stepCount) {
        this.userId = userId;
        this.day = day;
        this.timeInterval = timeInterval;
        this.stepCount = stepCount;
    }

    /** Getters and setters. */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
