package com.healthcare.dogtraining.Model;

public class PurchaseplanInsideData {
        String id, course_trining_id,
            name,course_trining_name,
            command_name,
            command_id,
            video,
            audio,
            thumbnail,
            image,
            date_time,plan_order_id,certificate_status,cirtificate;

    public PurchaseplanInsideData(String id, String course_trining_id, String name, String course_trining_name, String command_name, String command_id, String video, String audio, String thumbnail, String image, String date_time, String plan_order_id, String certificate_status, String cirtificate) {
        this.id = id;
        this.course_trining_id = course_trining_id;
        this.name = name;
        this.course_trining_name = course_trining_name;
        this.command_name = command_name;
        this.command_id = command_id;
        this.video = video;
        this.audio = audio;
        this.thumbnail = thumbnail;
        this.image = image;
        this.date_time = date_time;
        this.plan_order_id = plan_order_id;
        this.certificate_status = certificate_status;
        this.cirtificate = cirtificate;
    }

    public String getCirtificate() {
        return cirtificate;
    }

    public void setCirtificate(String cirtificate) {
        this.cirtificate = cirtificate;
    }

    public String getCertificate_status() {
        return certificate_status;
    }

    public void setCertificate_status(String certificate_status) {
        this.certificate_status = certificate_status;
    }

    public String getCourse_trining_name() {
        return course_trining_name;
    }

    public void setCourse_trining_name(String course_trining_name) {
        this.course_trining_name = course_trining_name;
    }

    public String getPlan_order_id() {
        return plan_order_id;
    }

    public void setPlan_order_id(String plan_order_id) {
        this.plan_order_id = plan_order_id;
    }

    public String getId() {
        return id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand_name() {
        return command_name;
    }

    public void setCommand_name(String command_name) {
        this.command_name = command_name;
    }

    public String getCommand_id() {
        return command_id;
    }

    public void setCommand_id(String command_id) {
        this.command_id = command_id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
