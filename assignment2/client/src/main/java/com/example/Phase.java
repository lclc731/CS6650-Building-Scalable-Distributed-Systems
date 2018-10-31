package com.example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ChangLiu on 10/27/18.
 */
public class Phase {
    private static final int MAX_STEP_COUNT = 5000;

    private String phaseType;
    private ClientEndPoint clientEndPoint;
    private int threads;
    private int userNum;
    private int dayNum;
    private int testNum;
    private int intervalStart, intervalEnd;

    private int totalRequest = 0;
    private List<Double> latencyList = new ArrayList<>();
    private List<Long> requestTimeList = new ArrayList<>();

    public synchronized void incrementTotalRequest() {
        totalRequest++;
    }
    public synchronized void addLatency(double latency) {
        latencyList.add(latency);
    }
    public synchronized void addRequestTime(long time) { requestTimeList.add(time); }

    /**
     * Constructor of Phase
     */
    public Phase(String phaseType, ClientEndPoint clientEndPoint, int threadNum, int userNum, int dayNum, int testNum) {
        this.phaseType = phaseType;
        this.clientEndPoint = clientEndPoint;
        this.userNum = userNum;
        this.dayNum = dayNum;
        this.testNum = testNum;
        switch (phaseType) {
            case "Warmup":
                this.threads = threadNum / 10;
                this.intervalStart = 0;
                this.intervalEnd = 2;
                break;
            case "Loading":
                this.threads = threadNum / 2;
                this.intervalStart = 3;
                this.intervalEnd = 7;
                break;
            case "Peak":
                this.threads = threadNum;
                this.intervalStart = 8;
                this.intervalEnd = 18;
                break;
            case "Cooldown":
                this.threads = threadNum / 4;
                this.intervalStart = 19;
                this.intervalEnd = 23;

        }
    }


    public void run() {
        Timestamp startTimestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("Client starting …..Time: " + startTimestamp);
        System.out.println(phaseType + ": " + threads + " threads running ….");

        final CountDownLatch latch = new CountDownLatch(threads);
        final int iterations = testNum * (intervalEnd - intervalStart + 1);

        try {
            for (int i = 0; i < this.threads; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < iterations; j++) {
                            int[] users = new int[3];
                            int[] intervals = new int[3];
                            int[] stepCounts = new int[3];
                            for (int k = 0; k < 3; k++) {
                                users[k] = ThreadLocalRandom.current().nextInt(1, userNum);
                                intervals[k] = ThreadLocalRandom.current().nextInt(intervalStart, intervalEnd + 1);
                                stepCounts[k] = ThreadLocalRandom.current().nextInt(1, MAX_STEP_COUNT + 1);
                            }

                            Timestamp curStart_1 = new Timestamp(System.currentTimeMillis());
                            String s1 = clientEndPoint.postStepCount(users[0], dayNum, intervals[0], stepCounts[0]);
                            Timestamp curEnd_1 = new Timestamp(System.currentTimeMillis());
                            incrementTotalRequest();
                            addRequestTime(curStart_1.getTime() / 1000);
                            addLatency((curEnd_1.getTime() - curStart_1.getTime()) / 1000.0);

                            Timestamp curStart_2 = new Timestamp(System.currentTimeMillis());
                            String s2 = clientEndPoint.postStepCount(users[1], dayNum, intervals[1], stepCounts[1]);
                            Timestamp curEnd_2 = new Timestamp(System.currentTimeMillis());
                            incrementTotalRequest();
                            addRequestTime(curStart_2.getTime() / 1000);
                            addLatency((curEnd_2.getTime() - curStart_2.getTime()) / 1000.0);

                            Timestamp curStart_3 = new Timestamp(System.currentTimeMillis());
                            int a1 = clientEndPoint.getCurrentDay(users[0]);
                            Timestamp curEnd_3 = new Timestamp(System.currentTimeMillis());
                            incrementTotalRequest();
                            addRequestTime(curStart_3.getTime() / 1000);
                            addLatency((curEnd_3.getTime() - curStart_3.getTime()) / 1000.0);

                            Timestamp curStart_4 = new Timestamp(System.currentTimeMillis());
                            int a2 = clientEndPoint.getSingleDay(users[1], dayNum);
                            Timestamp curEnd_4 = new Timestamp(System.currentTimeMillis());
                            incrementTotalRequest();
                            addRequestTime(curStart_4.getTime() / 1000);
                            addLatency((curEnd_4.getTime() - curStart_4.getTime()) / 1000.0);

                            Timestamp curStart_5 = new Timestamp(System.currentTimeMillis());
                            String s3 = clientEndPoint.postStepCount(users[2], dayNum, intervals[2], stepCounts[2]);
                            Timestamp curEnd_5 = new Timestamp(System.currentTimeMillis());
                            incrementTotalRequest();
                            addRequestTime(curStart_5.getTime() / 1000);
                            addLatency((curEnd_5.getTime() - curStart_5.getTime()) / 1000.0);
                        }
                        latch.countDown();
                    }
                }).start();
            }
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(phaseType + " complete: Time " +
                (endTimestamp.getTime() - startTimestamp.getTime()) / 1000.0 + " seconds");
    }

    public int getTotalRequest() {
        return totalRequest;
    }

    public List<Double> getLatencyList() {
        return latencyList;
    }

    public List<Long> getRequestTimeList() {
        return requestTimeList;
    }

}
