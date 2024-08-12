package com.example.recipeapp.Model;

public class Recipe {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    private String duration;

    public Recipe(String title, String duration, String description, String ingredients, String category, String imageUri,String YoutubeLink) {
        this.title = title;
        this.duration = duration;
        this.description = description;
        this.ingredients = ingredients;
        this.category = category;
        this.imageUri = imageUri;
        this.YoutubeLink=YoutubeLink;
    }

    public Recipe(){

    }
    private String description;

    public String getYoutubeLink() {
        return YoutubeLink;
    }

    public void setYoutubeLink(String YoutubeLink) {
        this.YoutubeLink = YoutubeLink;
    }

    private String YoutubeLink;
    private String ingredients;
    private String category;
    private String imageUri;


}
