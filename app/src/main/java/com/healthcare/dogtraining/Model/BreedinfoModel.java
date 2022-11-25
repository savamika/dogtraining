package com.healthcare.dogtraining.Model;

public class BreedinfoModel {
  String id,name,age,image,breed,weight,gender,status,created_at,updeted_at;


    public BreedinfoModel(String id, String name,
                          String age, String image, String breed,String weight,
                          String gender, String status, String created_at,
                          String updeted_at) {

        this.id=id;
        this.name=name;
        this.age=age;
        this.image=image;
        this.breed=breed;
        this.weight=weight;
        this.gender=gender;
        this.status=status;
        this.created_at=created_at;
        this.updeted_at=updeted_at;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUpdeted_at() {
        return updeted_at;
    }

    public void setUpdeted_at(String updeted_at) {
        this.updeted_at = updeted_at;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
