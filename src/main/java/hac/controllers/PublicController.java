package hac.controllers;

import hac.repo.ingredient.Ingredient;
import hac.repo.ingredient.IngredientRepository;
import hac.repo.rating.Rating;
import hac.repo.rating.RatingRepository;
import hac.repo.recipe.Recipe;
import hac.repo.recipe.RecipeRepository;
import hac.services.RecipeService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class PublicController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RecipeService recipeService;

    // Handles the GET request for the home page
    @GetMapping("/")
    public String index(Model model) {
        // Redirects to the feed page
        return "redirect:/feed";
    }

    // Handles the GET request for the login page
    @GetMapping("/login")
    public String login(Model model) {
        return "login/login";
    }

    // Handles the GET request to display the recipes in the feed
    @GetMapping("/feed")
    public String showRecipes(@RequestParam(required = false) String searchType,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) Integer maxCookingTime,
                              @RequestParam(required = false) String difficulty,
                              Rating rating, Model model) {
        List<Recipe> recipesList = getRecipesList(searchType, keyword, category, maxCookingTime, difficulty);
        Map<String, Integer> categoryCounts = getCategoryCounts();
        model.addAttribute("categoryCounts", categoryCounts);
        model.addAttribute("recipesList", recipesList);
        model.addAttribute("selectedCategory", category);
        return "recipe/feed";
    }

    // Handles the GET request to display a specific recipe
    @GetMapping("/recipe/{recipeId}")
    public String showRecipe(@PathVariable String recipeId, @RequestParam(required = false) Boolean error, Model model) {
        model.addAttribute("error", isErrorMessage(error));
        Recipe recipe = recipeService.getRecipeById(Long.parseLong(recipeId));
        String username = getLoggedInUserName();
        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredientsList", getIngredientsByRecipe(recipe));
        Rating ratePerUser = getRatingByUserAndRecipe(username, recipe);
        model.addAttribute("currRate", ratePerUser == null ? 0 : ratePerUser.getRate());
        double averageRating = getAverageRating(recipe);
        model.addAttribute("ratings", averageRating);
        return "recipe/recipe";
    }

    private Boolean isErrorMessage(Boolean error) {
        return error != null && error;
    }

    // Retrieves the username of the currently logged-in user
    private String getLoggedInUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        return username;
    }

    // Retrieves the category counts for recipes
    private Map<String, Integer> getCategoryCounts() {
        Map<String, Integer> categoryCounts = new HashMap<>();

        List<String> categories = Arrays.asList("All", "Appetizers", "Main-Courses",
                "Side-Dishes", "Desserts", "Breakfast", "Soups", "Baking", "Drinks",
                "Healthy", "Grilling", "Italian", "Chinese");

        for (String c : categories) {
            if (c.equals("All")) {
                categoryCounts.put(c, recipeRepository.findAll().size());
            } else {
                categoryCounts.put(c, recipeRepository.findByCategoryContainingIgnoreCase(c).size());
            }
        }
        return categoryCounts;
    }

    // Retrieves the list of recipes based on search parameters
    private List<Recipe> getRecipesList(String searchType, String keyword, String category,
                                        Integer maxCookingTime, String difficulty) {
        List<Recipe> recipesList;

        if (searchType != null && !Objects.equals(keyword, "")) {
            // Searching by author or recipe name
            recipesList = switch (searchType) {
                case "author" -> recipeRepository.findByAuthorContainingIgnoreCase(keyword);
                case "recipeName" -> recipeRepository.findByRecipeNameContainingIgnoreCase(keyword);
                default -> recipeRepository.findAll();
            };
        } else if (category != null && !category.equals("All")) {
            // Filtering by category
            recipesList = recipeRepository.findByCategoryContainingIgnoreCase(category);
        } else if (maxCookingTime != null) {
            // Filtering by max cooking time
            recipesList = recipeRepository.findByTimeBetween(0, maxCookingTime);
        } else if (difficulty != null) {
            // Filtering by difficulty
            recipesList = recipeRepository.findByDifficultyContainingIgnoreCase(difficulty);
        } else {
            // No filters applied, return all recipes
            recipesList = recipeRepository.findAll();
        }

        return recipesList;
    }

    // Retrieves the average rating for a recipe
    private double getAverageRating(Recipe recipe) {
        List<Rating> ratings = ratingRepository.findByRecipe(recipe);
        int sum = 0;
        for (Rating r : ratings) {
            sum += r.getRate();
        }
        if (ratings.size() == 0) {
            return 0;
        } else {
            double avg = (double) sum / ratings.size();
            avg = Math.round(avg * 10.0) / 10.0;
            return avg;
        }
    }

    // Retrieves the rating given by a specific user for a recipe
    private Rating getRatingByUserAndRecipe(String username, Recipe recipe) {
        return ratingRepository.findByUserNameAndRecipe(username, recipe);
    }

    // Retrieves the list of ingredients for a recipe
    private List<Ingredient> getIngredientsByRecipe(Recipe recipe) {
        return ingredientRepository.findByRecipe(recipe);
    }
}
