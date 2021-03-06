package com.example;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    private ClientEndPoint clientEndPoint;

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

        // Run four phases
        Phase phaseWarmUp = new Phase("Warmup", clientEndPoint, maxThreadNum, userNumber, dayNumber, testNumber);
        phaseWarmUp.run();
        Phase phaseLoading = new Phase("Loading", clientEndPoint, maxThreadNum, userNumber, dayNumber, testNumber);
        phaseLoading.run();
        Phase phasePeak = new Phase("Peak", clientEndPoint, maxThreadNum, userNumber, dayNumber, testNumber);
        phasePeak.run();
        Phase phaseCooldown = new Phase("Cooldown", clientEndPoint, maxThreadNum, userNumber, dayNumber, testNumber);
        phaseCooldown.run();

        Timestamp endWallTime = new Timestamp(System.currentTimeMillis());

        // Save results from four different phases together
        int totalRequest = phaseWarmUp.getTotalRequest() + phaseLoading.getTotalRequest()
                + phasePeak.getTotalRequest() + phaseCooldown.getTotalRequest();
        List<Long> totalLatency = new ArrayList<>();
        totalLatency.addAll(phaseWarmUp.getLatencyList());
        totalLatency.addAll(phaseLoading.getLatencyList());
        totalLatency.addAll(phasePeak.getLatencyList());
        totalLatency.addAll(phaseCooldown.getLatencyList());

        List<Long> requestList = new ArrayList<>();
        requestList.addAll(phaseWarmUp.getRequestTimeList());
        requestList.addAll(phaseLoading.getRequestTimeList());
        requestList.addAll(phasePeak.getRequestTimeList());
        requestList.addAll(phaseCooldown.getRequestTimeList());

        // Calculate and process the latency
        LatencyStatistic latencyStatistic = new LatencyStatistic(totalRequest, totalLatency, requestList);
        latencyStatistic.processStatistic();

        int meanThroughput = latencyStatistic.getMeanThroughput();
        long nintyFivePercentileLatency = latencyStatistic.getNintyFivePercentileLatency();
        long nintyNinePercentileLatency = latencyStatistic.getNintyNinePercentileLatency();

        double totalWallTime = (endWallTime.getTime() - startWallTime.getTime()) / 1000.0;

        // Print out all statistic results
        System.out.println("===================================================");
        System.out.println("Total run time (wall time) for all threads to complete: " + totalWallTime + " seconds");
        System.out.println("Total number of requests sent: " + totalRequest);
        System.out.println("Mean throughput is: " + meanThroughput);
        System.out.println("95th percentile latency is: " + nintyFivePercentileLatency / 1000.0);
        System.out.println("99th percentile latency is: " + nintyNinePercentileLatency / 1000.0);
    }
}
