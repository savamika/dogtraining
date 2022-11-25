package com.healthcare.dogtraining.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCoursenameModel {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("course_trainig_id")
    @Expose
    private String course_trainig_id;

    @SerializedName("course_id")
    @Expose
    private String course_id;




    @SerializedName("command")
    @Expose
    private List<SubnamecommandModel> timeSlotVaccs = null;



    public List<SubnamecommandModel> getTimeSlotVaccs() {
        return timeSlotVaccs;
    }

    public void setTimeSlotVaccs(List<SubnamecommandModel> timeSlotVaccs) {
        this.timeSlotVaccs = timeSlotVaccs;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_trainig_id() {
        return course_trainig_id;
    }

    public void setCourse_trainig_id(String course_trainig_id) {
        this.course_trainig_id = course_trainig_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
