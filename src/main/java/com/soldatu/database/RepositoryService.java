package com.soldatu.database;

import com.soldatu.model.Recipe;

import java.util.List;

/**
 * Created by florin.soldatu on 20/04/2020.
 */

public interface RepositoryService {
    List<Recipe> getAllRecipes();
}
