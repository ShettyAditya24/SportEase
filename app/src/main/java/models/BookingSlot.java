package models;

public class BookingSlot {
    private String timeSlot;
    private String clientName  ;   // The time slot available for booking

    // Default constructor for Firestore
    public BookingSlot() {}

    // Constructor for creating a new booking slot
    public BookingSlot(String timeSlot,String clientName) {
        this.timeSlot = timeSlot;
        this.clientName = clientName;
    }

    // Getters and Setters
    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
    public String getClientName() {
        return clientName; // Getter for client name
    }
}
