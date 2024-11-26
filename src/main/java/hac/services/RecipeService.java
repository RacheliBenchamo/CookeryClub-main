package hac.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import hac.repo.ingredient.Ingredient;
import hac.repo.ingredient.IngredientRepository;
import hac.repo.rating.RatingRepository;
import hac.repo.recipe.Recipe;
import hac.repo.recipe.RecipeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.util.Arrays;

/**
 * This class is responsible for handling all the business logic for the Recipe entity.
 */
@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

     // This method is responsible for adding a recipes to the database from a json file.
    public void loadRecipesFromJsonFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Recipe[] recipes = objectMapper.readValue(new File(filePath), Recipe[].class);
        recipeRepository.saveAll(Arrays.asList(recipes));
    }

    // This method is responsible for checking if a recipe exists in the database.
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + id));
    }

}
