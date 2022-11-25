package com.healthcare.dogtraining.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlot_Vacc {

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


