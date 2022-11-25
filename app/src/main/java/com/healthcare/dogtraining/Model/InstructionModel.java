package com.healthcare.dogtraining.Model;

public class InstructionModel {
String  id,command_details_id,title,instruction;

    public InstructionModel(String id, String command_details_id,
                            String title, String instruction) {

        this.id=id;
        this.command_details_id=command_details_id;
        this.title=title;
        this.instruction=instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommand_details_id() {
        return command_details_id;
    }

    public void setCommand_details_id(String command_details_id) {
        this.command_details_id = command_details_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
