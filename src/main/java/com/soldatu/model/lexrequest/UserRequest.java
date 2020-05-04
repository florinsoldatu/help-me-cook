package com.soldatu.model.lexrequest;

/**
 * Created by florin.soldatu on 06/02/2020.
 */
public class UserRequest {
    private String mealType;
    private String occasion;
    private String diet;
    private String desiredIngredient;
    private String undesiredIngredient;

     public UserRequest(String occasion, String mealType, String diet, String desiredIngredient, String undesiredIngredient) {
        this.occasion = occasion;
        this.mealType = mealType;
        this.diet = diet;
        this.desiredIngredient = desiredIngredient;
        this.undesiredIngredient = undesiredIngredient;
    }

    public String getMealType() {
        return mealType;
    }

    public String getOccasion() {
        return occasion;
    }

    public String getDiet() {
        return diet;
    }

    public String getDesiredIngredient() {
        return desiredIngredient;
    }

    public String getUndesiredIngredient() {
        return undesiredIngredient;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                ", MealType='" + mealType + '\'' +
                ", Occasion='" + occasion + '\'' +
                ", Diet='" + diet + '\'' +
                ", desiredIngredient=" + desiredIngredient +
                ", undesiredIngredient=" + undesiredIngredient +
                '}';
    }
}
