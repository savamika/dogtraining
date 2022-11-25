package com.healthcare.dogtraining.Model;

public class GetNewsFeedModel {
  String id,title,content,image,status,created_at,updated_at,like,comment,
          share;

    public GetNewsFeedModel(String id, String title, String content,
                            String image, String status, String created_at,
                            String updated_at, String like, String comment,
                            String share) {
        this.id=id;
        this.title=title;
        this.content=content;
        this.image=image;
        this.status=status;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.like=like;
        this.comment=comment;
        this.share=share;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
