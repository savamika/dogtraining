package com.healthcare.dogtraining.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubnamecommandModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("course_trining_id")
    @Expose
    private String course_trining_id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("command_name")
    @Expose
    private String command_name;

    @SerializedName("command_id")
    @Expose
    private String command_id;

    @SerializedName("video")
    @Expose
    private String video;

    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("date_time")
    @Expose
    private String date_time;

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCommand_id() {
        return command_id;
    }

    public void setCommand_id(String command_id) {
        this.command_id = command_id;
    }

    public String getId() {
        return id;
    }

    public String getCommand_name() {
        return command_name;
    }

    public void setCommand_name(String command_name) {
        this.command_name = command_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse_trining_id() {
        return course_trining_id;
    }

    public void setCourse_trining_id(String course_trining_id) {
        this.course_trining_id = course_trining_id;
    }
}




