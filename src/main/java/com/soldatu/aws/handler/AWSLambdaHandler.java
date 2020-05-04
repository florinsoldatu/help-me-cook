package com.soldatu.aws.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.soldatu.model.*;
import com.soldatu.model.lexrequest.UserRequest;
import com.soldatu.model.lexresponse.*;
import com.soldatu.service.RecipeService;

import java.util.LinkedHashMap;
import java.util.List;

import static com.soldatu.Constants.*;


public class AWSLambdaHandler implements RequestHandler<Object, LexResponse> {
    private RecipeService recipeService = new RecipeService();

    @Override
    public LexResponse handleRequest(Object object, Context context) {
        context.getLogger().log("\nGot request:  " + object);

        try {
            LinkedHashMap lexRequestHashMap = (LinkedHashMap) object;
            LinkedHashMap lexIntent = (LinkedHashMap) lexRequestHashMap.get(CURRENT_INTENT_KEY);
            String intentName = (String) lexIntent.get("name");
            context.getLogger().log("Received lex intent:  " + intentName);
            switch(intentName) {
                case GET_RECIPE_INTENT_NAME:
                    context.getLogger().log("Received get recipe intent");
                    LinkedHashMap userRequestAsMap = (LinkedHashMap) lexIntent.get("slots");

                    String occasion = (String) userRequestAsMap.get(OCCASION);
                    String mealType = (String) userRequestAsMap.get(MEAL_TYPE);
                    String diet = (String) userRequestAsMap.get(DIET);
                    String desiredIngredient = (String) userRequestAsMap.get(DESIRED_INGREDIENT);
                    String undesiredIngredient = (String) userRequestAsMap.get(UNDESIRED_INGREDIENT);

                    UserRequest userRequest = new UserRequest(occasion, mealType, diet, desiredIngredient, undesiredIngredient);
                    List<Recipe> recipes = recipeService.getRecipesForRequest(userRequest);
                    context.getLogger().log("\nFound recipes:" + recipes);
                    switch (recipes.size()) {
                        case 0:
                            return constructNoRecipeFoundResponse_ConfirmIntent();
                        case 1:
                            //search by lex request, but found one recipe, return that one and end flow
                            context.getLogger().log("\nSearch by lex request " + userRequest
                                    + ", but found one recipe " + recipes.get(0));
                            return constructOneRecipeLexResponse(recipes.get(0));
                        default:
                            //found multiple recipes to match the request
                            context.getLogger().log("\nFound multiple recipes to match the request " + recipes);
                            return constructMultipleRecipesLexResponse(userRequest, recipes);
                    }
                case GET_RECIPE_BY_NAME_INTENT_NAME:
                    context.getLogger().log("Received get recipe by name intent");
                    LinkedHashMap requestAsMap = (LinkedHashMap) lexIntent.get("slots");
                    String recipeName = (String) requestAsMap.get(RECIPE_NAME_SLOT);
                    Recipe recipe = recipeService.getRecipeByName(recipeName);
                    context.getLogger().log("Found one recipe:" + recipe);
                    return constructOneRecipeLexResponse(recipe);
                case TRY_AGAIN_INTENT_NAME:
                    context.getLogger().log("Received try again intent");
                    LinkedHashMap slotsMap = (LinkedHashMap) lexIntent.get("slots");
                    String tryAgainResponse = (String) slotsMap.get(TRY_AGAIN_RESPONSE_SLOT);
                    if (tryAgainResponse == null || tryAgainResponse.contains("no")
                            || tryAgainResponse.equalsIgnoreCase("no")
                            || tryAgainResponse.contains("not")
                            || tryAgainResponse.contains("don't")){
                        return constructGoodByeLexResponse();
                    } else {
                        return tryAgainLexResponse();
                    }
                default:
                    context.getLogger().log("\nReceived unknown intent:" + intentName);
                    return constructErrorCaseLexResponse();
            }
        } catch(Exception e){
            context.getLogger().log("\nOh oh, something went wrong, exception: " + e);
            return constructErrorCaseLexResponse();
        }
    }

    private LexResponse constructNoRecipeFoundResponse_ConfirmIntent(){
        PlainTextMessage message = new PlainTextMessage("Sorry, but for now I did not find any recipe matching your request. Is this fun or what?");
        ConfirmIntentDialogAction dialogAction = new ConfirmIntentDialogAction("ConfirmIntent", message);
        dialogAction.setIntentName(TRY_AGAIN_INTENT_NAME);
        return new LexResponse(dialogAction);
    }

    private LexResponse constructOneRecipeLexResponse(Recipe recipe){
        String recipeAsString = RespondToLex.createTextResponseFromOneRecipe(recipe);
        PlainTextMessage message = new PlainTextMessage(recipeAsString);
        DialogAction dialogAction = new DialogAction("Close", "Fulfilled", message);
        return new LexResponse(dialogAction);
    }

    private LexResponse constructMultipleRecipesLexResponse(UserRequest userRequest, List<Recipe> recipes){
        String recipesAsString = RespondToLex.createTextResponseFromRecipeList(recipes);
        PlainTextMessage message = new PlainTextMessage(recipesAsString);

        ElicitSlotDialogAction dialogAction = new ElicitSlotDialogAction();
        dialogAction.setType("ElicitSlot");
        dialogAction.setIntentName(GET_RECIPE_BY_NAME_INTENT_NAME);
        dialogAction.setSlotToElicit(RECIPE_NAME_SLOT);
        dialogAction.addSlot(MEAL_TYPE, userRequest.getMealType());
        dialogAction.setMessage(message);

        return new LexResponse(dialogAction);
    }

    private LexResponse constructErrorCaseLexResponse(){
        PlainTextMessage message = new PlainTextMessage("Sorry, I did not quite get that.");
        DialogAction dialogAction = new DialogAction("Close", "Fulfilled", message);
        return new LexResponse(dialogAction);
    }

    private LexResponse tryAgainLexResponse(){
        //redirect to the beginning - get recipe intent
        PlainTextMessage message = new PlainTextMessage("Ok, here we go again.");
        DialogAction dialogAction = new DialogAction("ElicitIntent", message);
        return new LexResponse(dialogAction);
    }

    private LexResponse constructGoodByeLexResponse(){
        PlainTextMessage message = new PlainTextMessage("Have fun and I'll see you next time");
        DialogAction dialogAction = new DialogAction("Close", "Fulfilled", message);
        return new LexResponse(dialogAction);
    }
}
