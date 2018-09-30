package com.example;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ChangLiu on 9/26/18.
 */
public class MultiThreadCall {
    private int iterationNum;
    private int threadNum;
    private int totalRequest = 0;
    private int totalResponse = 0;
    private List<Long> latencyList;

    private ClientEndPoint clientEndPoint;

    public synchronized void incrementTotalRequest () {
        totalRequest++;
    }

    public synchronized void incrementTotalResponse () {
        totalResponse++;
    }

    public synchronized void addLatency(long latency) {
        latencyList.add(latency);
    }

    public MultiThreadCall(int iterationNum, int threadNum, String uri) {
        this.iterationNum = iterationNum;
        this.threadNum = threadNum;
        this.latencyList = new LinkedList<>();
        clientEndPoint = new ClientEndPoint(uri);
    }

    public void start() {
        Timestamp startWallTime = new Timestamp(System.currentTimeMillis());

        runByPhase("Warmup");
        runByPhase("Loading");
        runByPhase("Peak");
        runByPhase("Cooldown");

        Timestamp endWallTime = new Timestamp(System.currentTimeMillis());

        // Calculate
        LatencyStatistic latencyStatistic = new LatencyStatistic(latencyList);
        latencyStatistic.processStatistic();

        double totalWallTime = (endWallTime.getTime() - startWallTime.getTime()) / 1000.0;
        BigDecimal throughput = new BigDecimal(totalRequest / totalWallTime).setScale(2,2);

        // Print out all statistic results
        System.out.println("===================================================");
        System.out.println("Total run time (wall time) for all threads to complete: " + totalWallTime + " seconds");
        System.out.println("Total number of requests sent: " + totalRequest);
        System.out.println("Total number of Successful responses: " + totalResponse);
        System.out.println("Overall throughput across all phases: " + throughput);
        System.out.println("Mean latency is: " + latencyStatistic.getMeanLatency() / 1000.0 + " seconds. "
                + "Median latency is: " + latencyStatistic.getMedianLatency() / 1000.0 + " seconds.");
        System.out.println("99th percentile latency is: " + latencyStatistic.getNintyNinePercentile() / 1000.0 + " seconds. "
                + "95th percentile latency is: " + latencyStatistic.getNintyFivePercentile() / 1000.0 + " seconds.");
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
                            Timestamp curStart_1 = new Timestamp(System.currentTimeMillis());
                            String status = clientEndPoint.getStatus();
                            Timestamp curEnd_1 = new Timestamp(System.currentTimeMillis());

                            Timestamp curStart_2 = new Timestamp(System.currentTimeMillis());
                            String postLen = clientEndPoint.postText("aaa", String.class);
                            Timestamp curEnd_2 = new Timestamp(System.currentTimeMillis());

                            incrementTotalRequest();
                            incrementTotalRequest();

                            if (status.equals("alive")) {
                                incrementTotalResponse();
                                addLatency(curEnd_1.getTime() - curStart_1.getTime());
                            }
                            if (postLen.equals("3")) {
                                incrementTotalResponse();
                                addLatency(curEnd_2.getTime() - curStart_2.getTime());
                            }
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
