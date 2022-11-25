package com.healthcare.dogtraining.Model;

public class CityModel {

   String city_id,city_name, state_id;

    public CityModel(String city_id, String city_name, String state_id) {
        this.city_id=city_id;
        this.city_name=city_name;
        this.state_id=state_id;

    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
}
