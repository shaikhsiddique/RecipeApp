package com.example.recipeapp.Model;

public class Category {
    private String title;

    public Category(String title, String imageUri) {
        this.title = title;
        this.imageUri = imageUri;
    }
public Category(){

}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    private String imageUri;

}
