package com.example.myapplication;
import java.util.HashMap;
import java.util.Map;
public class TrainScheduleEntry {
        private String trainId;
        private String trainName;
        private String source;
        private String destination;
        private Map<String, String> schedule;

        public TrainScheduleEntry(String trainId, String trainName, String source, String destination, String schedule) {
            this.trainId = trainId;
            this.trainName = trainName;
            this.source = source;
            this.destination = destination;
            this.schedule = new HashMap<>();
        }

        public String getTrainId() {
            return trainId;
        }

        public void setTrainId(String trainId) {
            this.trainId = trainId;
        }

        public String getTrainName() {
            return trainName;
        }

        public void setTrainName(String trainName) {
            this.trainName = trainName;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public Map<String, String> getSchedule() {
            return schedule;
        }

        public void setSchedule(Map<String, String> schedule) {
            this.schedule = schedule;
        }

        // Method to add station and its corresponding time to the schedule
        public void addStationTime(String station, String time) {
            schedule.put(station, time);
        }

        // Method to remove station from the schedule
        public void removeStation(String station) {
            schedule.remove(station);
        }
}


