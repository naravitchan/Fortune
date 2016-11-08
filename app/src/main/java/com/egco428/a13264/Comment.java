package com.egco428.a13264;

/**
 * Created by Seagame on 11/8/2016 AD.
 */
public class Comment {
    private long id;
    private String comment;
    private String date;
    private String image;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getPicture() {
        return image;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setPicture(String image) {
        this.image = image;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return comment;
    }
}
