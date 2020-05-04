package com.soldatu.database;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.soldatu.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by florin.soldatu on 04/02/2020.
 */

public class RepositoryServiceImpl implements RepositoryService {

    protected static final String TABLE_NAME = "Recipes";

    private static final String NAME_FIELD = "Name";
    private static final String DESCRIPTION_FIELD = "Description";
    private static final String IMAGE_URL_FIELD = "imageUrl";
    private static final String DIETS_FIELD = "specialDiets";
    private static final String OCCASIONS_FIELD = "occasions";
    private static final String INGREDIENTS_FIELD = "ingredients";
    private static final String MEAL_TYPES_FIELD = "mealTypes";

    private static RepositoryServiceImpl repositoryServiceInstance;

    private RepositoryServiceImpl(){}

    public static RepositoryServiceImpl getRepositoryInstance(){
        if (repositoryServiceInstance == null){
            repositoryServiceInstance = new RepositoryServiceImpl();
        }
        return repositoryServiceInstance;
    }

    public List<Recipe> getAllRecipes() {
        return getRecipesFromDynamoDB();
    }

    private List<Recipe> getMockRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe1 = new Recipe();
        recipe1.setName("Vegan omlette");
        recipe1.setDescription("Vegan omlette description");
        recipe1.setIngredients(Arrays.asList("chickpea", "tomatoes", "carrot", "zucchini"));
        recipe1.setMealTypes(Arrays.asList("breakfast", "brunch"));
        recipe1.setOccasions(Arrays.asList("weekend", "weekday", "family"));
        recipe1.setSpecialDiets(Arrays.asList("vegan", "gluten-free"));
        recipe1.setImageUrl("https://help-me-cook.s3.amazonaws.com/VeganOmlette.jpg");
        recipes.add(recipe1);

        Recipe recipe2 = new Recipe();
        recipe2.setName("Vegetarian Lasagna");
        recipe2.setDescription("Vegetarian Lasagna description");
        recipe2.setIngredients(Arrays.asList("eggplant", "tomatoes", "mozarella"));
        recipe2.setMealTypes(Arrays.asList("lunch"));
        recipe2.setOccasions(Arrays.asList("weekend", "family"));
        recipe2.setSpecialDiets(Arrays.asList("vegetarian"));
        recipe2.setImageUrl("https://help-me-cook.s3.amazonaws.com/VeganOmlette.jpg");
        recipes.add(recipe2);

        Recipe recipe3 = new Recipe();
        recipe3.setName("Broccoli sotee");
        recipe3.setDescription("broccoli sotee");
        recipe3.setIngredients(Arrays.asList("broccoli", "almonds", "butter"));
        recipe3.setMealTypes(Arrays.asList("lunch", "dinner"));
        recipe3.setOccasions(Arrays.asList("weekday"));
        recipe3.setSpecialDiets(Arrays.asList("vegetarian", "gluten-free", "light"));
        recipe3.setImageUrl("https://help-me-cook.s3.amazonaws.com/Brocoli-sote.jp");
        recipes.add(recipe3);

        Recipe recipe4 = new Recipe();
        recipe4.setName("Quinoa with vegetables");
        recipe4.setDescription("quinoa with vegetables");
        recipe4.setIngredients(Arrays.asList("quinoa", "peas", "carrot"));
        recipe4.setMealTypes(Arrays.asList("lunch", "dinner"));
        recipe4.setOccasions(Arrays.asList("weekday", "weekend"));
        recipe4.setSpecialDiets(Arrays.asList("vegetarian", "gluten-free", "vegan"));
        recipe4.setImageUrl("https://help-me-cook.s3.amazonaws.com/QuinoaWithVegetables.png");
        recipes.add(recipe4);

        Recipe recipe5 = new Recipe();
        recipe5.setName("Summer Ratatouille");
        recipe5.setDescription("summer ratatouille");
        recipe5.setIngredients(Arrays.asList("tomatoes", "carrot", "zucchini"));
        recipe5.setMealTypes(Arrays.asList("lunch", "dinner"));
        recipe5.setOccasions(Arrays.asList("weekend", "family"));
        recipe5.setSpecialDiets(Arrays.asList("vegan", "gluten-free", "light"));
        recipe5.setImageUrl("https://help-me-cook.s3.amazonaws.com/SummerRatatouille.png");
        recipes.add(recipe5);

        return recipes;
    }

    private List<Recipe> getRecipesFromDynamoDB(){
                Map<String, AttributeValue> lastKeyEvaluated = null;
        List<Recipe> recipes = new ArrayList<>();
        do {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName(TABLE_NAME)
                    .withLimit(10)
                    .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = connectToDynamo().scan(scanRequest);
            for (Map<String, AttributeValue> item : result.getItems()){
                Recipe recipe = new Recipe();

                AttributeValue nameAttribute = item.get(NAME_FIELD);
                recipe.setName(nameAttribute.getS());

                AttributeValue descriptionAttribute = item.get(DESCRIPTION_FIELD);
                recipe.setDescription(descriptionAttribute.getS());

                AttributeValue imageUrlAttribute = item.get(IMAGE_URL_FIELD);
                recipe.setImageUrl(imageUrlAttribute.getS());

                List<String> specialDiets = new ArrayList<>();
                AttributeValue dietsAttribute = item.get(DIETS_FIELD);
                dietsAttribute.getL().forEach(attributeValue -> specialDiets.add(attributeValue.getS()));
                recipe.setSpecialDiets(specialDiets);

                List<String> occasions = new ArrayList<>();
                AttributeValue occasionsAttribute = item.get(OCCASIONS_FIELD);
                occasionsAttribute.getL().forEach(attributeValue -> occasions.add(attributeValue.getS()));
                recipe.setOccasions(occasions);

                List<String> ingredients = new ArrayList<>();
                AttributeValue ingredientsAttribute = item.get(INGREDIENTS_FIELD);
                ingredientsAttribute.getL().forEach(attributeValue -> ingredients.add(attributeValue.getS()));
                recipe.setIngredients(ingredients);

                List<String> mealTypes = new ArrayList<>();
                AttributeValue mealTypesAttribute = item.get(MEAL_TYPES_FIELD);
                mealTypesAttribute.getL().forEach(attributeValue -> mealTypes.add(attributeValue.getS()));
                recipe.setMealTypes(mealTypes);

                recipes.add(recipe);
            }
            lastKeyEvaluated = result.getLastEvaluatedKey();
        } while (lastKeyEvaluated != null);

        return recipes;
    }

    protected static AmazonDynamoDB connectToDynamo(){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider
                        (new BasicAWSCredentials("AKIATH5GYMGLDJXAR6ED","yC1J/xR1y8f/3Ai5aDmYcu275VGxKD+mpaYRDbiY")))
                .build();
        return client;
    }
}
