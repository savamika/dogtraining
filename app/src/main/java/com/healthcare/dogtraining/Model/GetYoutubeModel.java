package com.healthcare.dogtraining.Model;

public class GetYoutubeModel {
   String id,youtube_link,thumbnail;


    public GetYoutubeModel(String id, String youtube_link,String thumbnail) {
        this.id=id;
        this.youtube_link=youtube_link;
        this.thumbnail=thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getYoutube_link() {
        return youtube_link;
    }

    public void setYoutube_link(String youtube_link) {
        this.youtube_link = youtube_link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
