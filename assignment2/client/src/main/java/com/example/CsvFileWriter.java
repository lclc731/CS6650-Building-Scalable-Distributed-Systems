package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ChangLiu on 10/30/18.
 */
public class CsvFileWriter {
    // Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    // CSV file header
    private static final String FILE_HEADER = "time,count";

    // CSV file name
    private static final String FILE_NAME = "test.csv";

    public void writeToCSVFile(Map<Long, Integer> requestMap, List<Long> keyList, long minTime) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(FILE_NAME);
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (long key : keyList) {
                int timeDiff = (int) (key - minTime);
                fileWriter.append(String.valueOf(timeDiff));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(requestMap.get(key)));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

}
