package com.example.sportease;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Ground implements Parcelable {
    private String clubName;
    private String address;
    private String openTime;
    private String closeTime;
    private String description;
    private List<String> imageUrls;
    private String clubOwnerId; // Added to link ground with its club owner
    private String groundId; // Added to uniquely identify this ground

    // No-argument constructor for Firestore
    public Ground() {}

    // Constructor for Parcelable
    protected Ground(Parcel in) {
        clubName = in.readString();
        address = in.readString();
        openTime = in.readString();
        closeTime = in.readString();
        description = in.readString();
        imageUrls = in.createStringArrayList();
        clubOwnerId = in.readString(); // Reading clubOwnerId from Parcel
        groundId = in.readString();    // Reading groundId from Parcel
    }

    // Getters and setters for all fields
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
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

    // Write object data to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clubName);
        dest.writeString(address);
        dest.writeString(openTime);
        dest.writeString(closeTime);
        dest.writeString(description);
        dest.writeStringList(imageUrls != null ? imageUrls : new ArrayList<>());
        dest.writeString(clubOwnerId);
        dest.writeString(groundId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable.Creator implementation
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
}
