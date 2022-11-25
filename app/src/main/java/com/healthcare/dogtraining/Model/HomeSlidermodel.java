package com.healthcare.dogtraining.Model;

public class HomeSlidermodel {
   String id,image;

    public HomeSlidermodel(String id, String image) {
        this.id=id;
        this.image=image;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
