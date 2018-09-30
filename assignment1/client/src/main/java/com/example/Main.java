package com.example;


import java.io.IOException;

/**
 * Created by ChangLiu on 9/20/18.
 */
public class Main {
    private static final int DEFAULT_MAX_THREAD_NUM = 50;
    private static final int DEFAULT_ITERATION_NUM_PER_THREAD = 100;

    public static void main(String[] args) throws IOException {
        if (args == null || args.length != 4) {
            throw new IllegalArgumentException("Invalid number of input arguments.");
        }
        int maxThreadNum = args[0] == null || args[0].length() == 0 ? DEFAULT_MAX_THREAD_NUM :
                Integer.parseInt(args[0]);
        int iterationNumPerThread =  args[1] == null || args[1].length() == 0 ? DEFAULT_ITERATION_NUM_PER_THREAD :
                Integer.parseInt(args[1]);
        String ipAddress = args[2];
        String port = args[3];

        String uri = "http://" + ipAddress + ":" + port + "/simple-service-webapp/rest/myresource";
        MultiThreadCall multiThreadCall = new MultiThreadCall(iterationNumPerThread, maxThreadNum, uri);
        multiThreadCall.start();
    }

}
