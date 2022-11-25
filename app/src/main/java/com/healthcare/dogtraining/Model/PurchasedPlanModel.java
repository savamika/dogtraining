package com.healthcare.dogtraining.Model;

public class PurchasedPlanModel {

   String  course_id,
    course_trainig_id,
            name;

    public PurchasedPlanModel(String course_id, String course_trainig_id, String name) {

        this.course_id = course_id;
        this.course_trainig_id = course_trainig_id;
        this.name = name;
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
