package com.healthcare.dogtraining.Adapter;

public class Command_model {
    String Commandname;
    String course_trining_id;
    String plan_name;
    String id;
String select_command_limit, is_all;

    public Command_model(String commandname, String course_trining_id, String plan_name, String id, String select_command_limit, String is_all) {
        Commandname = commandname;
        this.course_trining_id = course_trining_id;
        this.plan_name = plan_name;
        this.id = id;
        this.select_command_limit = select_command_limit;
        this.is_all = is_all;
    }

    public String getCommandname() {
        return Commandname;
    }

    public void setCommandname(String commandname) {
        Commandname = commandname;
    }

    public String getCourse_trining_id() {
        return course_trining_id;
    }

    public void setCourse_trining_id(String course_trining_id) {
        this.course_trining_id = course_trining_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelect_command_limit() {
        return select_command_limit;
    }

    public void setSelect_command_limit(String select_command_limit) {
        this.select_command_limit = select_command_limit;
    }

    public String getIs_all() {
        return is_all;
    }

    public void setIs_all(String is_all) {
        this.is_all = is_all;
    }
}
