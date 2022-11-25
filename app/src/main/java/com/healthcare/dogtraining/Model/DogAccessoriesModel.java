package com.healthcare.dogtraining.Model;

public class DogAccessoriesModel {

    String id,name,product_type,image,status,created_at,updated_at;

    public DogAccessoriesModel(String id, String name, String product_type,
                        String image, String status, String created_at,
                        String updated_at) {
        this.id=id;
        this.name=name;
        this.product_type=product_type;
        this.image=image;
        this.status=status;
        this.created_at=created_at;
        this.updated_at=updated_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
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


