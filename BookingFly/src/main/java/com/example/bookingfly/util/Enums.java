package com.example.bookingfly.util;

public class Enums {
    public static enum Role {
        USER, ADMIN
    }

    public static enum AccountStatus {
        ACTIVE, DELETED, INACTIVE, BLOCKED
    }

    public static enum AirlineStatus {
        ACTIVE, DELETED, INACTIVE
    }

    public static enum AirlineArea {
        INLAND, INTERNATIONAL
    }

    public static enum AirportStatus {
        ACTIVE, DELETED, INACTIVE
    }

    public static enum FlightStatus {
        ACTIVE, DELETED, INACTIVE
    }

    public static enum BookingStatus {
        COMPLETED, EXPIRED, PROCESSING, CANCELED, DELETED
    }

    public static enum Gender {
        MALE, FEMALE, OTHER
    }

    public static enum PassengerStatus {
        ACTIVE, DELETED, INACTIVE
    }

    public static enum ReviewStatus {
        ACTIVE, DELETED, INACTIVE, BLOCKED
    }

    public static enum PaymentMethod {
        IMMEDIATE, CARDCREDIT, ELECTRONICWALLET
    }

    public static enum PaymentStatus {
        PAID, UNPAID, DELETED
    }
}
