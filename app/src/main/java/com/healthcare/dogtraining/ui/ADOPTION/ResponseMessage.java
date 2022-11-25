package com.healthcare.dogtraining.ui.ADOPTION;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMessage {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("msg")
    @Expose
    private String msg;


    @SerializedName("data")
    @Expose
    private String data;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ResponseMessage withResult(String result) {
        this.result = result;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResponseMessage withMsg(String msg) {
        this.msg = msg;
        return this;
    }


}
