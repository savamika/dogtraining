package com.healthcare.dogtraining.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VaccinationTimeDatum {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("command_name")
    @Expose
    private String command_name;


    @SerializedName("plan_name")
    @Expose
    private String plan_name;


    @SerializedName("course_trining_id")
    @Expose
    private List<TimeSlot_Vacc> timeSlotVaccs = null;

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TimeSlot_Vacc> getTimeSlotVaccs() {
        return timeSlotVaccs;
    }

    public void setTimeSlotVaccs(List<TimeSlot_Vacc> timeSlotVaccs) {
        this.timeSlotVaccs = timeSlotVaccs;
    }
}
