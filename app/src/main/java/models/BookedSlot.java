package models;

import android.os.Parcel;
import android.os.Parcelable;

public class BookedSlot implements Parcelable {
    private String id;         // Store either userId or coachId
    private String bookedBy;   // Indicates whether the booking was made by "user" or "coach"
    private String groundId;   // Ground ID field
    private String timeSlot;

    // Default constructor required for Firestore
    public BookedSlot() {}

    public BookedSlot(String id, String bookedBy, String groundId, String timeSlot) {
        this.id = id;                     // Initialize ID
        this.bookedBy = bookedBy;         // Initialize bookedBy
        this.groundId = groundId;         // Initialize groundId
        this.timeSlot = timeSlot;
      // Initialize timeSlot
    }

    protected BookedSlot(Parcel in) {
        id = in.readString();             // Read ID
        bookedBy = in.readString();       // Read bookedBy
        groundId = in.readString();       // Read groundId
        timeSlot = in.readString();       // Read timeSlot
    }

    public static final Creator<BookedSlot> CREATOR = new Creator<BookedSlot>() {
        @Override
        public BookedSlot createFromParcel(Parcel in) {
            return new BookedSlot(in);
        }

        @Override
        public BookedSlot[] newArray(int size) {
            return new BookedSlot[size];
        }
    };

    // Getters and Setters
    public String getId() {
        return id;  // Getter for ID (can be userId or coachId)
    }

    public void setId(String id) {
        this.id = id;  // Setter for ID
    }

    public String getBookedBy() {
        return bookedBy;  // Getter for bookedBy ("user" or "coach")
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;  // Setter for bookedBy
    }

    public String getGroundId() {
        return groundId;  // Getter for groundId
    }

    public void setGroundId(String groundId) {
        this.groundId = groundId;  // Setter for groundId
    }

    public String getTimeSlot() {
        return timeSlot;  // Getter for timeSlot
    }


    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;  // Setter for timeSlot
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);            // Write ID
        dest.writeString(bookedBy);      // Write bookedBy
        dest.writeString(groundId);      // Write groundId
        dest.writeString(timeSlot);      // Write timeSlot
    }
}