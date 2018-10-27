package com.example;

import java.sql.Timestamp;
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
                this.intervalStart = 8;
                this.intervalEnd = 18;
                break;
            case "Cooldown":
                this.intervalStart = 19;
                this.intervalEnd = 23;
                this.threads = threadNum / 4;
        }
    }


    public void run() {
        Timestamp startTimestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("Client starting …..Time: " + startTimestamp);
        System.out.println(phaseType + ": " + threads + " threads running ….");

        final CountDownLatch latch = new CountDownLatch(threads);
        final int iterations = this.testNum * (this.intervalEnd - this.intervalStart + 1);

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
                            clientEndPoint.postStepCount(users[0], dayNum, intervals[0], stepCounts[0]);
                            clientEndPoint.postStepCount(users[1], dayNum, intervals[1], stepCounts[1]);
                            clientEndPoint.getCurrentDay(users[0]);
                            clientEndPoint.getSingleDay(users[1], dayNum);
                            clientEndPoint.postStepCount(users[2], dayNum, intervals[2], stepCounts[2]);
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
}
