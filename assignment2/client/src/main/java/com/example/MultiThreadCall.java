package com.example;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ChangLiu on 9/26/18.
 */
public class MultiThreadCall {
    private static final int WARMUP_INTERVAL_START = 0;
    private static final int WARMUP_INTERVAL_END = 2;
    private static final int LOADING_INTERVAL_START = 3;
    private static final int LOADING_INTERVAL_END = 7;
    private static final int PEAK_INTERVAL_START = 8;
    private static final int PEAK_INTERVAL_END = 18;
    private static final int COOLDOWN_INTERVAL_START = 19;
    private static final int COOLDOWN_INTERVAL_END = 23;

    private int maxThreadNum;
    private int userNumber;
    private int dayNumber;
    private int testNumber;

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

    /**
     * Constructor of MultiThreadCall
     */
    public MultiThreadCall(String uri, int maxThreadNum, int userNumber, int dayNumber, int testNumber) {
        this.maxThreadNum = maxThreadNum;
        this.userNumber = userNumber;
        this.dayNumber = dayNumber;
        this.testNumber = testNumber;
        clientEndPoint = new ClientEndPoint(uri);
    }

    /**
     * Start execute the MultiThreadCall
     */
    public void start() {
        clientEndPoint.deleteTable();

        Timestamp startWallTime = new Timestamp(System.currentTimeMillis());

        Phase phaseWarmUp = new Phase("Warmup", clientEndPoint, maxThreadNum, userNumber, dayNumber, testNumber);
        phaseWarmUp.run();
        Phase phaseLoading = new Phase("Loading", clientEndPoint, maxThreadNum, userNumber, dayNumber, testNumber);
        phaseLoading.run();
        Phase phasePeak = new Phase("Peak", clientEndPoint, maxThreadNum, userNumber, dayNumber, testNumber);
        phasePeak.run();
        Phase phaseCooldown = new Phase("Cooldown", clientEndPoint, maxThreadNum, userNumber, dayNumber, testNumber);
        phaseCooldown.run();

        Timestamp endWallTime = new Timestamp(System.currentTimeMillis());

        // Calculate
//        LatencyStatistic latencyStatistic = new LatencyStatistic(latencyList);
//        latencyStatistic.processStatistic();

        double totalWallTime = (endWallTime.getTime() - startWallTime.getTime()) / 1000.0;
//        BigDecimal throughput = new BigDecimal(totalRequest / totalWallTime).setScale(2,2);
//
        // Print out all statistic results
        System.out.println("===================================================");
        System.out.println("Total run time (wall time) for all threads to complete: " + totalWallTime + " seconds");
//        System.out.println("Total number of requests sent: " + totalRequest);
//        System.out.println("Total number of Successful responses: " + totalResponse);
//        System.out.println("Overall throughput across all phases: " + throughput);
//        System.out.println("Mean latency is: " + latencyStatistic.getMeanLatency() / 1000.0 + " seconds. "
//                + "Median latency is: " + latencyStatistic.getMedianLatency() / 1000.0 + " seconds.");
//        System.out.println("99th percentile latency is: " + latencyStatistic.getNintyNinePercentile() / 1000.0 + " seconds. "
//                + "95th percentile latency is: " + latencyStatistic.getNintyFivePercentile() / 1000.0 + " seconds.");
    }


}
