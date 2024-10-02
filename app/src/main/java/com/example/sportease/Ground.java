package com.example.sportease;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Ground implements Parcelable {
    private String clubName;      // Name of the club/ground
    private String address;        // Address of the ground
    private String openTime;       // Opening time of the ground
    private String closeTime;      // Closing time of the ground
    private List<String> imageUrls; // List of image URLs for the ground
    private String groundId;       // Unique identifier for the ground
    private List<String> availableSlots; // List of available slots

    // Default constructor for Firestore
    public Ground() {}

    public Ground(String clubName, String address, String openTime, String closeTime,
                  List<String> imageUrls, String groundId, List<String> availableSlots) {
        this.clubName = clubName;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.imageUrls = imageUrls;
        this.groundId = groundId;
        this.availableSlots = availableSlots;
    }

    protected Ground(Parcel in) {
        clubName = in.readString();
        address = in.readString();
        openTime = in.readString();
        closeTime = in.readString();
        imageUrls = in.createStringArrayList();
        groundId = in.readString();
        availableSlots = in.createStringArrayList();
    }

    public static final Creator<Ground> CREATOR = new Creator<Ground>() {
        @Override
        public Ground createFromParcel(Parcel in) {
            return new Ground(in);
        }

        @Override
        public Ground[] newArray(int size) {
            return new Ground[size];
        }
    };

    // Getters and Setters
    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getGroundId() {
        return groundId;
    }

    public void setGroundId(String groundId) {
        this.groundId = groundId;
    }

    public List<String> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<String> availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clubName);
        dest.writeString(address);
        dest.writeString(openTime);
        dest.writeString(closeTime);
        dest.writeStringList(imageUrls);
        dest.writeString(groundId);
        dest.writeStringList(availableSlots);
    }
}
