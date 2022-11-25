package com.healthcare.dogtraining.Model;

public class ChatMessage {
    String senderID;
    String receiveerID;
    String message;
    String username;
    String image;
    String video;
    String time;
    String Recording;
    String status;
    String planname;
    String command_id;
    String plan_order_id;

    public ChatMessage(String senderID, String receiveerID, String message, String username, String image, String video, String time, String recording, String status, String planname, String command_id, String plan_order_id) {
        this.senderID = senderID;
        this.receiveerID = receiveerID;
        this.message = message;
        this.username = username;
        this.image = image;
        this.video = video;
        this.time = time;
        Recording = recording;
        this.status = status;
        this.planname = planname;
        this.command_id = command_id;
        this.plan_order_id = plan_order_id;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getCommand_id() {
        return command_id;
    }

    public void setCommand_id(String command_id) {
        this.command_id = command_id;
    }

    public String getPlan_order_id() {
        return plan_order_id;
    }

    public void setPlan_order_id(String plan_order_id) {
        this.plan_order_id = plan_order_id;
    }

    public String getRecording() {
        return Recording;
    }

    public void setRecording(String recording) {
        Recording = recording;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiveerID() {
        return receiveerID;
    }

    public void setReceiveerID(String receiveerID) {
        this.receiveerID = receiveerID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
