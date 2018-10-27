package com.example;


import java.io.IOException;

/**
 * Created by ChangLiu on 9/20/18.
 */
public class Main {
    private static final int DEFAULT_MAX_THREAD_NUM = 64;
    private static final int DEFAULT_DAY_NUMBER = 1;
    private static final int DEFAULT_USER_NUMBER = 100000;
    private static final int DEFAULT_TEST_NUMBER = 100;

    public static void main(String[] args) throws IOException {
        String ipAddress = args[0];
        int maxThreadNum = args[1] == null || args[1].length() == 0 ? DEFAULT_MAX_THREAD_NUM :
                Integer.parseInt(args[1]);
        int userNumber = args[2] == null || args[2].length() == 0 ? DEFAULT_USER_NUMBER :
                Integer.parseInt(args[2]);
        int dayNumber = args[3] == null || args[3].length() == 0 ? DEFAULT_DAY_NUMBER :
                Integer.parseInt(args[3]);
        int testNumber = args[4] == null || args[4].length() == 0 ? DEFAULT_TEST_NUMBER :
                Integer.parseInt(args[4]);

        String uri = "http://" + ipAddress + ":8080/simple-service-webapp/webapi/fitbit";
//        String uri = "https://mpx5u5ssvl.execute-api.us-west-2.amazonaws.com/server-api/myresource";
        MultiThreadCall multiThreadCall = new MultiThreadCall(uri, maxThreadNum, userNumber, dayNumber, testNumber);
        multiThreadCall.start();
    }

}
