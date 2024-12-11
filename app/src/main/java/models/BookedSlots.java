package models;

public class BookedSlots {
    private String id;          // Coach or user ID
    private String bookedBy;    // "user" or "coach"
    private String groundId;
    private String timeSlot;
    private String coachName;
    private String clubName;

    public BookedSlots(String id, String bookedBy, String groundId, String timeSlot, String coachName, String clubName) {
        this.id = id;
        this.bookedBy = bookedBy;
        this.groundId = groundId;
        this.timeSlot = timeSlot;
        this.coachName = coachName;
        this.clubName = clubName;
    }

    // Getters
    public String getId() { return id; }
    public String getBookedBy() { return bookedBy; }
    public String getGroundId() { return groundId; }
    public String getTimeSlot() { return timeSlot; }
    public String getCoachName() { return coachName; }
    public String getClubName() { return clubName; }
}
