package com.healthcare.dogtraining.Model;

public class GetImagesModel {

   String id,adop_id, images;

    public GetImagesModel(String id, String adop_id, String images) {
        this.id=id;
        this.adop_id=adop_id;
        this.images=images;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAdop_id() {
        return adop_id;
    }

    public void setAdop_id(String adop_id) {
        this.adop_id = adop_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
