package com.soldatu.model.lexresponse;

import com.soldatu.model.Recipe;

import java.util.List;

/**
 * Created by florin.soldatu on 01/05/2020.
 */
public class RespondToLex {

    private static String nameTag = "RecipeName";
    private static String descriptionTag = "RecipeDescription";
    private static String imageURLTag = "RecipeURL";

    /**
     * If only one recipe, add name + description + imageUrl
     *
     * @param recipe
     * @return a text description of the recipe
     */
    public static String createTextResponseFromOneRecipe(Recipe recipe){
        if (recipe.getDescription() != null) {
            return nameTag + recipe.getName() + descriptionTag + recipe.getDescription() + imageURLTag + recipe.getImageUrl();
        } else {
            return nameTag + recipe.getName() + imageURLTag + recipe.getImageUrl();
        }
    }

    /**
     * If multiple recipes, add only their names + imageUrls
     *
     * @param recipes
     * @return a text description of the recipes
     */
    public static String createTextResponseFromRecipeList(List<Recipe> recipes){
        String finalValue = "";
        for (Recipe recipe: recipes){
            finalValue = finalValue + createTextResponseFromOneRecipe(recipe);
        }
        return finalValue;
    }
}
