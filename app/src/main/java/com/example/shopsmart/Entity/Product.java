package com.example.shopsmart.Entity;

public class Product {
    private int id;
    private String name;
    private int idCategory;
    private int toPrice;
    private int fromPrice;
    private String urlImage;
    private String description;
    private int active;

    public Product(int id, String name, int idCategory, int toPrice, int fromPrice, String urlImage, String description, int active) {
        this.id = id;
        this.name = name;
        this.idCategory = idCategory;
        this.toPrice = toPrice;
        this.fromPrice = fromPrice;
        this.urlImage = urlImage;
        this.description = description;
        this.active = active;
    }

    public int getToPrice() {
        return toPrice;
    }

    public void setToPrice(int toPrice) {
        this.toPrice = toPrice;
    }

    public int getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(int fromPrice) {
        this.fromPrice = fromPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
