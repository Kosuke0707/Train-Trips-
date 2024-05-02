package com.example.myapplication;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public static List<TrainScheduleEntry> parseCSV(String filePath) throws IOException, CsvException {
        List<TrainScheduleEntry> scheduleEntries = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            // Read the CSV file
            List<String[]> rows = reader.readAll();

            // Skip the header row
            boolean isFirstRow = true;
            for (String[] row : rows) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip header row
                }

                // Extract relevant information from each row
                String trainId = row[0];
                String trainName = row[1];
                String source = row[2];
                String destination = row[3];
                String schedule = row[4];

                // Create a new TrainScheduleEntry object and add it to the list
                TrainScheduleEntry entry = new TrainScheduleEntry(trainId, trainName, source, destination, schedule);
                scheduleEntries.add(entry);
            }
        }

        return scheduleEntries;
    }
}
