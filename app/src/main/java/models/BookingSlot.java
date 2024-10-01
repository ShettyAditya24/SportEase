package models;

public class BookingSlot {
    private String timeSlot;   // The time slot available for booking

    // Default constructor for Firestore
    public BookingSlot() {}

    // Constructor for creating a new booking slot
    public BookingSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    // Getters and Setters
    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
