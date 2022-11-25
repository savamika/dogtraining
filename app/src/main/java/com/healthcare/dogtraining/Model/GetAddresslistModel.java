package com.healthcare.dogtraining.Model;

public class GetAddresslistModel {

String id,user_id,address,city,postcode,city_name;

    public GetAddresslistModel(String id, String user_id, String address,
                               String city, String postcode, String city_name) {
        this.id=id;
        this.user_id=user_id;
        this.address=address;
        this.city=city;
        this.postcode=postcode;
        this.city_name=city_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
