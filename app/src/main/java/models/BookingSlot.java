package models;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingSlot implements Parcelable {
    private String timeSlot;
    private String clubName;
    private String clubOwnerId;
    private String groundId;
    private String coachName; // New field for coach name

    public BookingSlot() {
    }
    // Full constructor
    public BookingSlot(String timeSlot, String clubName, String clubOwnerId, String groundId, String name) {
        this.timeSlot = timeSlot;
        this.clubName = clubName;
        this.clubOwnerId = clubOwnerId;
        this.groundId = groundId;
        this.coachName = name; // Initialize coach name
    }

    // Optional constructor (for convenience)
    public BookingSlot(String timeSlot, String groundId) {
        this(timeSlot, "", null, groundId, ""); // Call the main constructor with empty coach name
    }

    // Getters and setters
    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubOwnerId() {
        return clubOwnerId;
    }

    public void setClubOwnerId(String clubOwnerId) {
        this.clubOwnerId = clubOwnerId;
    }

    public String getGroundId() {
        return groundId;
    }

    public void setGroundId(String groundId) {
        this.groundId = groundId;
    }

    public String getCoachName() { // Getter for coach name
        return coachName;
    }

    public void setCoachName(String coachName) { // Setter for coach name
        this.coachName = coachName;
    }

    // Parcelable implementation
    protected BookingSlot(Parcel in) {
        timeSlot = in.readString();
        clubName = in.readString();
        clubOwnerId = in.readString();
        groundId = in.readString();
        coachName = in.readString(); // Read coach name from Parcel
    }

    public static final Creator<BookingSlot> CREATOR = new Creator<BookingSlot>() {
        @Override
        public BookingSlot createFromParcel(Parcel in) {
            return new BookingSlot(in);
        }

        @Override
        public BookingSlot[] newArray(int size) {
            return new BookingSlot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timeSlot);
        dest.writeString(clubName);
        dest.writeString(clubOwnerId);
        dest.writeString(groundId);
        dest.writeString(coachName); // Write coach name to Parcel
    }
}
