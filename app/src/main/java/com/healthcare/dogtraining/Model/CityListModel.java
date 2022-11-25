package com.healthcare.dogtraining.Model;

public class CityListModel {

   String city_name_,spin_city_id;

    public CityListModel(String city_name_, String spin_city_id) {
        this.city_name_=city_name_;
        this.spin_city_id=spin_city_id;
    }

    public String getSpin_city_id() {
        return spin_city_id;
    }

    public void setSpin_city_id(String spin_city_id) {
        this.spin_city_id = spin_city_id;
    }

    public String getCity_name_() {
        return city_name_;
    }

    public void setCity_name_(String city_name_) {
        this.city_name_ = city_name_;
    }
}
