package com.example.shopsmart.Entity;

public class ColorProduct {
    private int idColor;
    private int Product;
    private String urlImage;
    private String color;

    public int getIdColor() {
        return idColor;
    }

    public void setIdColor(int idColor) {
        this.idColor = idColor;
    }

    public int getProduct() {
        return Product;
    }

    public void setProduct(int product) {
        Product = product;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getColorCode() {
        return color;
    }

    public void setColorCode(String colorCode) {
        this.color = colorCode;
    }

    public ColorProduct(int idColor,int product , String urlImage, String colorCode) {
        this.idColor = idColor;
        this.Product=product;
        this.urlImage = urlImage;
        this.color = colorCode;
    }
}
