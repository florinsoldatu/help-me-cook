package com.soldatu.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by florin.soldatu on 06/02/2020.
 */
public class Recipe {

    private String name;
    private String description;
    private String imageUrl;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMealTypes(List<String> mealTypes) {
        this.mealTypes = mealTypes;
    }

    public void setOccasions(List<String> occasions) {
        this.occasions = occasions;
    }

    public void setSpecialDiets(List<String> specialDiets) {
        this.specialDiets = specialDiets;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", mealTypes=" + mealTypes +
                ", occasions=" + occasions +
                ", specialDiets=" + specialDiets +
                ", ingredients=" + ingredients +
                '}';
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> mealTypes = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> occasions = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> specialDiets = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> ingredients = new ArrayList<>();

    public boolean matchesMealType(String mealType){
       return (mealType == null) ? true : mealTypes.stream().anyMatch(type -> mealType.equalsIgnoreCase(type) || mealType.contains(type));
    }

    public boolean matchesOccasion(String occasion){
        return (occasion == null) ? true : occasions.stream().anyMatch(type -> occasion.equalsIgnoreCase(type) || occasion.contains(type));
    }

    public boolean matchesSpecialDiets(String diet){
        return (diet == null) ? true : specialDiets.stream().anyMatch(type -> diet.equalsIgnoreCase(type) || diet.contains(type));
    }

    public boolean containsIngredient(String desiredIngredient){
        if (desiredIngredient == null ) return true;
        boolean match = false;
        for (String ingredient: ingredients){
            if (ingredient.equalsIgnoreCase(desiredIngredient) || desiredIngredient.contains(ingredient)){
                return true;
            }
        }
        return match;
    }

    public boolean doesNotContainIngredient(String undesiredIngredient){
        return (undesiredIngredient == null) ? true : !ingredients.contains(undesiredIngredient.toLowerCase());
    }
}
