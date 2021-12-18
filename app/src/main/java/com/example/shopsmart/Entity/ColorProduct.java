package com.example.shopsmart.Entity;

public class ColorProduct {
    private int idColor;
    private int Product;
    private String urlImage;
    private String colorCode;

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
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public ColorProduct(int idColor, int product, String urlImage, String colorCode) {
        this.idColor = idColor;
        Product = product;
        this.urlImage = urlImage;
        this.colorCode = colorCode;
    }
}
