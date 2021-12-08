package com.example.shopsmart.Entity;

public class Category {
    private int id;
    private String title;
    private String urlImage;
    private String description;

    public Category(int id, String title, String urlImage, String description) {
        this.id = id;
        this.title = title;
        this.urlImage = urlImage;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
