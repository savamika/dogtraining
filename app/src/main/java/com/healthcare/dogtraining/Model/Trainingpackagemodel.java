package com.healthcare.dogtraining.Model;

public class Trainingpackagemodel {

   String id,name,image,created_date;

    public Trainingpackagemodel(String id, String name, String image,
                                String created_date) {

        this.id=id;
        this.name=name;
        this.image=image;
        this.created_date=created_date;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
