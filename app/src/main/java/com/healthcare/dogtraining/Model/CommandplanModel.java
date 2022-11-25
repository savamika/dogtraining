package com.healthcare.dogtraining.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommandplanModel {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("command_id")
    @Expose
    private String command_id;

    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("instruction")
    @Expose
    private String instruction;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("date_time")
    @Expose
    private String date_time;



}
