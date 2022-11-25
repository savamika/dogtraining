package com.healthcare.dogtraining.Model;

public class StateListModel {

   String  state_name,spin_state_id;

    public StateListModel(String state_name, String spin_state_id) {
        this.state_name=state_name;
        this.spin_state_id=spin_state_id;
    }

    public String getSpin_state_id() {
        return spin_state_id;
    }

    public void setSpin_state_id(String spin_state_id) {
        this.spin_state_id = spin_state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}
