package com.example.fbstore;

public class User {
    public String fullName;
    public String email;
    public long signupTime;
    public String profileImage; // Base64-encoded image string

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String fullName, String email, long signupTime, String profileImage) {
        this.fullName = fullName;
        this.email = email;
        this.signupTime = signupTime;
        this.profileImage = profileImage;
    }

    // Optional: Getters and setters (Firebase doesn't require them if fields are public)
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public long getSignupTime() {
        return signupTime;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSignupTime(long signupTime) {
        this.signupTime = signupTime;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

