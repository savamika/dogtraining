package com.healthcare.dogtraining.ui.chat;

public class GetAdminChatModel {
    String id,admin,user_id,message,chat_image,chat_video,chat_audio,type,types,date_time,fullname,image;
    public GetAdminChatModel(String id, String admin, String user_id,
                             String message,String chat_image,String chat_video,String chat_audio,
                             String type,String types,
                             String date_time, String fullname, String image) {

        this.id=id;
        this.admin=admin;
        this.user_id=user_id;
        this.message=message;
        this.chat_image=chat_image;
        this.chat_video=chat_video;
        this.chat_audio=chat_audio;
        this.type=type;
        this.types=types;
        this.date_time=date_time;
        this.fullname=fullname;
        this.image=image;
        }


    public String getChat_audio() {
        return chat_audio;
    }

    public void setChat_audio(String chat_audio) {
        this.chat_audio = chat_audio;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getChat_video() {
        return chat_video;
        }

    public void setChat_video(String chat_video) {
        this.chat_video = chat_video;
    }

    public String getChat_image() {
        return chat_image;
    }

    public void setChat_image(String chat_image) {
        this.chat_image = chat_image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
