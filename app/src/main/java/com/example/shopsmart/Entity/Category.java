package com.example.shopsmart.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "urlImage")
    private String urlImage;

    @ColumnInfo(name = "description")
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
