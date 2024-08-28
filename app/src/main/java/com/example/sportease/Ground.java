package com.example.sportease;

import android.util.Log;

public class Ground {

    private String imageUrl;
    private String clubName;
    private String address;
    private String openTime;
    private String closeTime;

    // Parameterized constructor
    public Ground(String imageUrl, String clubName, String address, String openTime, String closeTime) {
        this.imageUrl = imageUrl;
        this.clubName = clubName;
        this.address = address;
        this.openTime = openTime;
        this.closeTime = closeTime;
        Log.d("GroundClass", "Parameterized constructor called with values: " +
                "imageUrl=" + imageUrl + ", clubName=" + clubName + ", address=" + address +
                ", openTime=" + openTime + ", closeTime=" + closeTime);
    }

    // Default constructor required for calls to DataSnapshot.getValue(Ground.class)
    public Ground() {
        Log.d("GroundClass", "Default constructor called.");
    }

    // Getters and setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        Log.d("GroundClass", "Set imageUrl: " + imageUrl);
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
        Log.d("GroundClass", "Set clubName: " + clubName);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        Log.d("GroundClass", "Set address: " + address);
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
        Log.d("GroundClass", "Set openTime: " + openTime);
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
        Log.d("GroundClass", "Set closeTime: " + closeTime);
    }

    // Method to get formatted description
    public String getFormattedDescription() {
        String description = "Club Name: " + (clubName != null ? clubName : "N/A") + "\n" +
                "Address: " + (address != null ? address : "N/A") + "\n" +
                "Open Time: " + (openTime != null ? openTime : "N/A") + "\n" +
                "Close Time: " + (closeTime != null ? closeTime : "N/A");
        Log.d("GroundClass", "Formatted description: " + description);
        return description;
    }
}
