package hac.controllers.UserControllers;

import hac.repo.rating.Rating;
import hac.repo.rating.RatingRepository;
import hac.repo.recipe.Recipe;
import hac.repo.recipe.RecipeRepository;
import hac.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static java.lang.Integer.parseInt;

@Controller
public class UserRatingController {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RecipeService recipeService;

    // Handles the POST request to add or update a rating for a specific recipe
    @PostMapping("/user/rate/{recipeId}")
    public String addRate(@RequestParam("currRate") String currRate, @PathVariable String recipeId, Model model) {
        Recipe recipe = recipeService.getRecipeById(Long.parseLong(recipeId));

        // Retrieves the currently authenticated user's details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        // Checks if the user has already rated the recipe
        Rating rate = ratingRepository.findByUserNameAndRecipe(username, recipe);

        if (rate == null) {
            // If the user hasn't rated the recipe yet, create a new Rating object and save it
            Rating rating = new Rating(parseInt(currRate), username, recipe);
            ratingRepository.save(rating);
        } else {
            // If the user has already rated the recipe, update the existing rating
            rate.setRate(parseInt(currRate));
            ratingRepository.save(rate);
        }

        return "redirect:/recipe/" + recipeId;
    }
}
