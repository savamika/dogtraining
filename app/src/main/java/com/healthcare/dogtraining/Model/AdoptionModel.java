package com.healthcare.dogtraining.Model;

public class AdoptionModel {
    String id,user_id,name,age,gender,breed,weight,image,description,status,created_at,

    update_at;

    public AdoptionModel(String id, String user_id, String name,
                         String age, String gender, String breed,
                         String weight,String image, String description,
                         String status, String created_at, String update_at) {

        this.id=id;
        this.user_id=user_id;
        this.name=name;
        this.age=age;
        this.gender=gender;
        this.breed=breed;
        this.weight=weight;
        this.image=image;

        this.description=description;
        this.status=status;
        this.created_at=created_at;
        this.update_at=update_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
