package com.healthcare.dogtraining.Model;

public class Imagemodel {

    String id,bareed_id,images;

    public Imagemodel(String id, String bareed_id, String images) {

        this.id=id;
        this.bareed_id=bareed_id;
        this.images=images;

    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBareed_id() {
        return bareed_id;
    }

    public void setBareed_id(String bareed_id) {
        this.bareed_id = bareed_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
