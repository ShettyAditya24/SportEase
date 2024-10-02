package models;

import android.os.Parcel;
import android.os.Parcelable;

public class BookedSlot implements Parcelable {
    private String userId;    // User ID of the person who booked the slot
    private String groundId;  // ID of the ground for which the slot is booked
    private String timeSlot;   // The time slot booked

    // Empty constructor for Firestore deserialization
    public BookedSlot() {
    }

    // Updated constructor to include groundId
    public BookedSlot(String userId, String groundId, String timeSlot) {
        this.userId = userId;
        this.groundId = groundId;
        this.timeSlot = timeSlot;
    }

    protected BookedSlot(Parcel in) {
        userId = in.readString();
        groundId = in.readString();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroundId() {
        return groundId;
    }

    public void setGroundId(String groundId) {
        this.groundId = groundId;
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
        dest.writeString(groundId);  // Write groundId to parcel
        dest.writeString(timeSlot);
    }
}
