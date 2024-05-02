package com.example.myapplication;

public class Ticket {
        private String messageId;
        private String source;
        private String destination;
        private String transactionId;
        private String dateTime;

        public Ticket() {
            // Default constructor required for Firebase
        }

        public Ticket(String messageId, String source, String destination, String transactionId, String dateTime) {
            this.messageId = messageId;
            this.source = source;
            this.destination = destination;
            this.transactionId = transactionId;
            this.dateTime = dateTime;
        }

        // Getters and setters

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
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

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
    }


