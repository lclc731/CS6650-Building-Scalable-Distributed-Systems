package com.example;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ChangLiu on 9/26/18.
 */
public class MultiThreadCall {
    private int iterationNum;
    private int threadNum;
    private int totalRequest = 0;
    private int totalResponse = 0;

    private ClientEndPoint clientEndPoint;

    public synchronized void incrementTotalRequest () {
        totalRequest++;
    }

    public synchronized void incrementTotalResponse () {
        totalResponse++;
    }

    public MultiThreadCall(int iterationNum, int threadNum, String uri) {
        this.iterationNum = iterationNum;
        this.threadNum = threadNum;
        clientEndPoint = new ClientEndPoint(uri);
    }

    public void start() {
        Timestamp startWallTime = new Timestamp(System.currentTimeMillis());

        runByPhase("Warmup");
        runByPhase("Loading");
        runByPhase("Peak");
        runByPhase("Cooldown");

        Timestamp endWallTime = new Timestamp(System.currentTimeMillis());

        System.out.println("===================================================");
        System.out.println("Total number of requests sent: " + totalRequest);
        System.out.println("Total number of Successful responses: " + totalResponse);
        System.out.println("Test Wall Time: " + (endWallTime.getTime() - startWallTime.getTime()) / 1000.0 + " seconds");
    }

    public void runByPhase(String phase) {
        int threads = threadNum;
        switch (phase) {
            case "Warmup":
                threads /= 10;
                break;
            case "Loading":
                threads /= 2;
                break;
            case "Peak":
                break;
            case "Cooldown":
                threads /= 4;
        }

        Timestamp startTimestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("Client starting …..Time: " + startTimestamp);
        System.out.println(phase + ": " + threads + " threads running ….");

        final CountDownLatch latch = new CountDownLatch(threads);

        try {
            for (int i = 0; i < threads; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < iterationNum; j++) {
                            String status = clientEndPoint.getStatus();
                            String postLen = clientEndPoint.postText("aaa", String.class);
                            incrementTotalRequest();
                            incrementTotalRequest();
                            if (status.equals("alive")) incrementTotalResponse();
                            if (postLen.equals("3")) incrementTotalResponse();
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
        System.out.println(phase + " complete: Time " +
                (endTimestamp.getTime() - startTimestamp.getTime()) / 1000.0 + " seconds");
    }
}
