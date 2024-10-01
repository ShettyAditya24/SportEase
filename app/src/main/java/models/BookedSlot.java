package models;

public class BookedSlot {
    private String userId;    // ID of the user who booked the slot
    private String timeSlot;   // The time slot that was booked

    // Default constructor for Firestore
    public BookedSlot() {}

    // Constructor for creating a new booked slot
    public BookedSlot(String userId, String timeSlot) {
        this.userId = userId;
        this.timeSlot = timeSlot;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
