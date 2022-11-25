package com.healthcare.dogtraining.Model;

public class SortProductModel {

  String  id,cat_id,name,price,discounted_price,discounted_percentage,image,image1,
          image2,image3,image4,color,age,status,created_at,updated_at,description;


    public SortProductModel(String id, String cat_id,
                            String name, String price, String discounted_price,
                            String discounted_percentage, String image,
                            String image1, String image2, String image3,
                            String image4, String color, String age,
                            String status, String created_at,
                            String updated_at, String description) {
        this.id=id;
        this.cat_id=cat_id;
        this.name=name;
        this.price=price;
        this.discounted_price=discounted_price;
        this.discounted_percentage=discounted_percentage;
        this.image=image;
        this.image1=image1;
        this.image2=image2;
        this.image3=image3;
        this.image4=image4;
        this.color=color;
        this.age=age;
        this.status=status;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscounted_percentage() {
        return discounted_percentage;
    }

    public void setDiscounted_percentage(String discounted_percentage) {
        this.discounted_percentage = discounted_percentage;
    }

    public String getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(String discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
