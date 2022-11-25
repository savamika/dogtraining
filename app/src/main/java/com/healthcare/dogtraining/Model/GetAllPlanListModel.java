package com.healthcare.dogtraining.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllPlanListModel {
    @SerializedName("result")
    @Expose
    private String result;



    @SerializedName("data")
    private List<GetCoursenameModel> vaccinationTimeDatum = null;



    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<GetCoursenameModel> getVaccinationTimeDatum() {
        return vaccinationTimeDatum;
    }

    public void setVaccinationTimeDatum(List<GetCoursenameModel> data) {
        this.vaccinationTimeDatum = data;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}






