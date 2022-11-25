package com.healthcare.dogtraining.Model;

public class GetCommentListModel {

   String id,user_id,news_id,comment,create_date,date,fullname,image;

    public GetCommentListModel(String id, String user_id,
                               String news_id, String comment,
                               String create_date, String date,
                               String fullname, String image) {


        this.id=id;
        this.user_id=user_id;
        this.news_id=news_id;
        this.comment=comment;
        this.create_date=create_date;
        this.date=date;
        this.fullname=fullname;
        this.image=image;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
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
