package hac.controllers.UserControllers;

import hac.repo.ingredient.Ingredient;
import hac.repo.ingredient.IngredientRepository;
import hac.repo.recipe.Recipe;
import hac.repo.recipe.RecipeRepository;
import hac.services.RecipeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class UserRecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeService recipeService;

    // Handles the GET request to display the user's recipes
    @GetMapping("/user/my-recipes")
    public String getEditRecipes(Model model) {
        String username = getLoggedInUserName();
        List<Recipe> userRecipes = getUserRecipes(username);
        model.addAttribute("recipesList", userRecipes);
        return "recipe/my-recipes";
    }

    // Handles the GET request to display the add recipe page
    @GetMapping("/user/add/recipe")
    public String getAddRecipe(Recipe recipe, Model model) {
        return "recipe/add/add-recipe";
    }

    // Handles the POST request to add a new recipe
    @PostMapping("/user/add/recipe")
    public String addRecipe(@Valid Recipe recipe, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "recipe/add/add-recipe";
        }
        recipeRepository.save(recipe);
        return "redirect:/user/add/ingredient/" + recipe.getRecipeId() + "?action=add";
    }

    // Handles the GET request to display the add ingredient page for a specific recipe
    @GetMapping("/user/add/ingredient/{recipeId}")
    public String getAddIngredient(@PathVariable String recipeId, @RequestParam String action, Ingredient ingredient, Model model) {
        Recipe recipe = getRecipeById(recipeId);
        checkIfUserIsAuthor(recipe);
        model.addAttribute("ingredientsList", getIngredientsByRecipe(recipe));
        model.addAttribute("action", action);
        model.addAttribute("recipeId", recipeId);
        return "recipe/add/add-ingredient";
    }

    // Handle the POST request to add an ingredient to a specific recipe
    @PostMapping("/user/add/ingredient/{recipeId}")
    public String addIngredient(@Valid Ingredient ingredient, BindingResult result,
                                @PathVariable("recipeId") String recipeId,
                                @RequestParam("action") String action, Model model) {
        if (result.hasErrors()) {
            Recipe recipe = getRecipeById(recipeId);
            model.addAttribute("ingredientsList", getIngredientsByRecipe(recipe));
            model.addAttribute("action", action);
            model.addAttribute("recipeId", recipeId);
            return "recipe/add/add-ingredient";
        }

        Recipe recipe = getRecipeById(recipeId);
        ingredient.setRecipe(recipe);
        ingredientRepository.save(ingredient);
        return "redirect:/user/add/ingredient/" + recipeId + "?action=" + action;
    }

    // Handles the GET request to display the add instructions page for a specific recipe
    @GetMapping("/user/add/instructions/{recipeId}")
    public String getAddInstructions(@PathVariable String recipeId, Model model) {
        Recipe recipe = getRecipeById(recipeId);
        checkIfUserIsAuthor(recipe);
        if(recipe.getIngredients().isEmpty())
            throw new IllegalArgumentException("Recipe must have at least one ingredient to have instructions");
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("error", null);
        return "recipe/add/add-instructions";
    }

    // Handles the POST request to add instructions to a specific recipe
    @PostMapping("/user/add/instructions/{recipeId}")
    public String addInstructions(@RequestParam String instructions, @PathVariable("recipeId") String recipeId, Model model) {
        Recipe recipe = getRecipeById(recipeId);
        if (instructions == null || instructions.trim().isEmpty()) {
            model.addAttribute("recipeId", recipeId);
            model.addAttribute("error", "Instructions cannot be empty");
            return "recipe/add/add-instructions";
        }
        recipe.setInstructions(instructions);
        recipeRepository.save(recipe);
        return "redirect:/";
    }

    // Handles the GET request to display the edit recipe page for a specific recipe
    @GetMapping("/user/edit/recipe/{recipeId}")
    public String getEditRecipe(@PathVariable("recipeId") String recipeId, Model model) {
        Recipe recipe = getRecipeById(recipeId);
        checkIfUserIsAuthor(recipe);
        model.addAttribute("recipe", recipe);
        model.addAttribute("recipeId", recipeId);
        return "recipe/edit/edit-recipe";
    }

    // Handles the POST request to edit a specific recipe
    @PostMapping("/user/edit/recipe/{recipeId}")
    public String editRecipe(@Valid Recipe editedRecipe, BindingResult result, @PathVariable("recipeId") String recipeId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("recipeId", recipeId);
            return "recipe/edit/edit-recipe";
        }
        Recipe existingRecipe = getRecipeById(editedRecipe.getRecipeId().toString());

        existingRecipe.setRecipeName(editedRecipe.getRecipeName());
        existingRecipe.setCategory(editedRecipe.getCategory());
        existingRecipe.setTime(editedRecipe.getTime());
        existingRecipe.setPicture(editedRecipe.getPicture());
        existingRecipe.setDifficulty(editedRecipe.getDifficulty());

        recipeRepository.save(existingRecipe);
        return "redirect:/user/edit/ingredient/" + recipeId;
    }

    // Handles the GET request to display the edit ingredient page for a specific recipe
    @GetMapping("/user/edit/ingredient/{recipeId}")
    public String getEditIngredient(@PathVariable String recipeId, Ingredient ingredient, Model model) {
        Recipe recipe = getRecipeById(recipeId);
        checkIfUserIsAuthor(recipe);
        model.addAttribute("ingredientsList", getIngredientsByRecipe(recipe));
        model.addAttribute("recipeId", recipeId);
        return "recipe/edit/edit-ingredient";
    }

    // Handles the POST request to edit an ingredient of a specific recipe
    @PostMapping("/user/edit/ingredient/{recipeId}")
    public String postEditIngredient(@PathVariable String recipeId, @RequestParam String action,
                                     @Valid Ingredient ingredient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Recipe recipe = getRecipeById(recipeId);
            model.addAttribute("ingredientsList", getIngredientsByRecipe(recipe));
            model.addAttribute("recipeId", recipeId);
            model.addAttribute("currIngredientId", ingredient.getId());
            return "recipe/edit/edit-ingredient";
        }

        switch (action) {
            case "update" -> {
                Ingredient i = getIngredientById(ingredient.getId());
                i.setQuantity(ingredient.getQuantity());
                i.setUnit(ingredient.getUnit());
                ingredientRepository.save(i);
            }
            case "delete" -> {
                ingredientRepository.deleteById(ingredient.getId());
            }
            default -> throw new IllegalArgumentException("Invalid action: " + action);
        }
        return "redirect:/user/edit/ingredient/" + recipeId;
    }

    // Handles the GET request to display the edit instructions page for a specific recipe
    @GetMapping("/user/edit/instructions/{recipeId}")
    public String getEditInstructions(@PathVariable String recipeId, Model model) {
        Recipe recipe = getRecipeById(recipeId);
        checkIfUserIsAuthor(recipe);
        if(recipe.getIngredients().isEmpty())
            throw new IllegalArgumentException("Recipe must have at least one ingredient to have instructions");
        model.addAttribute("instructions", recipe.getInstructions());
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("error", null);
        return "recipe/edit/edit-instructions";
    }

    // Handles the POST request to edit the instructions of a specific recipe
    @PostMapping("/user/edit/instructions/{recipeId}")
    public String editInstructions(@RequestParam String instructions, @PathVariable("recipeId") String recipeId, Model model) {
        Recipe recipe = getRecipeById(recipeId);
        if (instructions == null || instructions.isEmpty()) {
            model.addAttribute("error", "Instructions cannot be empty");
            model.addAttribute("instructions", recipe.getInstructions());
            model.addAttribute("user", getLoggedInUserName());
            return "recipe/edit/edit-instructions";
        }
        recipe.setInstructions(instructions);
        recipeRepository.save(recipe);
        return "redirect:/";
    }

    // Helper method to get the username of the currently logged-in user
    private String getLoggedInUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        return username;
    }

    // Helper method to retrieve a recipe by its ID
    private Recipe getRecipeById(String recipeId) {
        return recipeService.getRecipeById(Long.parseLong(recipeId));
    }

    // Helper method to get a list of recipes belonging to a specific user
    private List<Recipe> getUserRecipes(String username) {
        return recipeRepository.findByAuthor(username);
    }

    // Helper method to retrieve a list of ingredients for a specific recipe
    private List<Ingredient> getIngredientsByRecipe(Recipe recipe) {
        return ingredientRepository.findByRecipe(recipe);
    }

    // Helper method to retrieve an ingredient by its ID
    private Ingredient getIngredientById(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ingredient Id:" + ingredientId));
    }

    // Helper method to check if the currently logged-in user is the author of a recipe
    private void checkIfUserIsAuthor(Recipe recipe) {
        if (!Objects.equals(recipe.getAuthor(), getLoggedInUserName())) {
            throw new IllegalArgumentException("You are not the author of this recipe");
        }
    }
}
