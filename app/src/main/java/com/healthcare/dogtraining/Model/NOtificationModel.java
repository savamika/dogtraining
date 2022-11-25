package com.healthcare.dogtraining.Model;

public class NOtificationModel {
  String id,title, description,image,receiver_id,seen_status,created_date;


    public NOtificationModel(String id, String title,
                             String description, String image, String receiver_id,
                             String seen_status, String created_date) {

      this.id=id;
      this.title=title;
      this.description=description;
      this.image=image;
      this.receiver_id=receiver_id;
      this.seen_status=seen_status;
      this.created_date=created_date;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getSeen_status() {
        return seen_status;
    }

    public void setSeen_status(String seen_status) {
        this.seen_status = seen_status;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
