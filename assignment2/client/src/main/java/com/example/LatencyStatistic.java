package com.example;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by ChangLiu on 9/29/18.
 */
public class LatencyStatistic {
    private long sumLatencies;
    private long medianLatency;
    private long meanLatency;
    private long nintyNinePercentile;
    private long nintyFivePercentile;

    private PriorityQueue<Long> queue = new PriorityQueue<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return (int) (o2 - o1);
        }
    });

    public LatencyStatistic(List<Long> latencyList) {
        for (Long latency : latencyList) {
            queue.offer(latency);
        }
    }

    public void processStatistic() {
        int total = queue.size();
        int nintyNineTotal = (int) (total * 0.01);
        int nintyFiveTotal = (int) (total * 0.05);
        int medianTotal = total / 2;

        for (int i = 1; i <= total; i++) {
            long cur = queue.poll();
            if (i == nintyNineTotal) {
                nintyNinePercentile = cur;
            } else if (i == nintyFiveTotal) {
                nintyFivePercentile = cur;
            } else if (i == medianTotal) {
                medianLatency = cur;
            }
            sumLatencies += cur;
        }
        meanLatency = sumLatencies / total;
    }

    public long getSumLatencies() {
        return sumLatencies;
    }

    public long getMedianLatency() {
        return medianLatency;
    }

    public long getMeanLatency() {
        return meanLatency;
    }

    public long getNintyNinePercentile() {
        return nintyNinePercentile;
    }

    public long getNintyFivePercentile() {
        return nintyFivePercentile;
    }

}
