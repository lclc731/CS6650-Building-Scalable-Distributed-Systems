package com.example;

import java.util.*;

/**
 * Created by ChangLiu on 9/29/18.
 */
public class LatencyStatistic {
    private int meanThroughput;
    private long nintyNinePercentileLatency;
    private long nintyFivePercentileLatency;

    private List<Long> latencyList;
    private List<Long> requestTimeList;
    private int totalRequestNum;

    /**
     * Constructor
     */
    public LatencyStatistic(int totalRequestNum, List<Long> latencyList, List<Long> requestTimeList) {
        this.totalRequestNum = totalRequestNum;
        this.latencyList = latencyList;
        this.requestTimeList = requestTimeList;
    }

    /**
     * The main function to do the statistic processing, including saving throughput to a csv file and
     * calculating mean throughput, 95p, 99p latency.
     */
    public void processStatistic() {
        if (requestTimeList == null || requestTimeList.size() == 0) {
            throw new IllegalArgumentException("No request to process.");
        }

        // Save request number by every second
        long minTime = Long.MAX_VALUE;
        Map<Long, Integer> requestMap = new HashMap<>();
        for (Long requestTime : requestTimeList) {
            requestMap.put(requestTime, requestMap.getOrDefault(requestTime, 0) + 1);
            minTime = minTime < requestTime ? minTime : requestTime;
        }
        List<Long> keyList = new ArrayList<>(requestMap.keySet());
        Collections.sort(keyList);

        // Write to a csv file
        CsvFileWriter csvFileWriter = new CsvFileWriter();
        csvFileWriter.writeToCSVFile(requestMap, keyList, minTime);

        // Calculate mean throughput
        List<Integer> throughputList = new ArrayList<>();
        int sumThroughtput = 0;
        for (long key : keyList) {
            throughputList.add(requestMap.get(key));
            sumThroughtput += requestMap.get(key);
        }
        Collections.sort(throughputList);
        meanThroughput = sumThroughtput / requestMap.size();

        // Calculate 95p, 99p latency
        int nintyNineTotal = (int) (latencyList.size() * 0.01);
        int nintyFiveTotal = (int) (latencyList.size() * 0.05);
        Collections.sort(latencyList);
        nintyFivePercentileLatency = latencyList.get(nintyFiveTotal);
        nintyNinePercentileLatency = latencyList.get(nintyNineTotal);
    }

    public int getMeanThroughput() {
        return meanThroughput;
    }

    public long getNintyNinePercentileLatency() {
        return nintyNinePercentileLatency;
    }

    public long getNintyFivePercentileLatency() {
        return nintyFivePercentileLatency;
    }
}
