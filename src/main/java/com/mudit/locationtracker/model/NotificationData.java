package com.mudit.locationtracker.model;

public class NotificationData {
    private String title;
    private String message;
    private String userId;
    private String payload;


    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
    public NotificationData(String title, String message, String userId, String payload){
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.payload = payload;
    }
    public NotificationData(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
