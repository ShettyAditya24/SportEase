package models;

import android.os.Parcel;
import android.os.Parcelable;

public class BookedSlot implements Parcelable {
    private String userId;
    private String groundId; // Add groundId field
    private String timeSlot;

    // Default constructor required for Firestore
    public BookedSlot() {}

    public BookedSlot(String userId, String groundId, String timeSlot) {
        this.userId = userId;
        this.groundId = groundId; // Initialize groundId
        this.timeSlot = timeSlot;
    }

    protected BookedSlot(Parcel in) {
        userId = in.readString();
        groundId = in.readString(); // Read groundId
        timeSlot = in.readString();
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
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroundId() {
        return groundId; // Getter for groundId
    }

    public void setGroundId(String groundId) {
        this.groundId = groundId; // Setter for groundId
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(groundId); // Write groundId
        dest.writeString(timeSlot);
    }
}
