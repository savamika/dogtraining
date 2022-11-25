package com.healthcare.dogtraining.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllDataModel {
    @SerializedName("result")
    @Expose
    private String result;



    @SerializedName("data")
    private List<VaccinationTimeDatum> vaccinationTimeDatum = null;



    @SerializedName("msg")
    @Expose
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<VaccinationTimeDatum> getVaccinationTimeDatum() {
        return vaccinationTimeDatum;
    }

    public void setVaccinationTimeDatum(List<VaccinationTimeDatum> data) {
        this.vaccinationTimeDatum = data;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}




