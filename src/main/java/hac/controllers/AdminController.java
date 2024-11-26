package hac.controllers;

import hac.repo.comment.Comment;
import hac.repo.comment.CommentRepository;
import hac.repo.rating.Rating;
import hac.repo.rating.RatingRepository;
import hac.repo.recipe.Recipe;
import hac.repo.recipe.RecipeRepository;
import hac.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    // Resets all comments for a specific recipe
    @PostMapping("/admin/comment/reset/{recipeId}")
    public String resetComment(@PathVariable String recipeId) {
        Recipe recipe = recipeService.getRecipeById(Long.parseLong(recipeId));
        List<Comment> comments = commentRepository.findByRecipe(recipe);
        commentRepository.deleteAll(comments);
        return "redirect:/recipe/" + recipeId;
    }

    // Resets all ratings for a specific recipe
    @PostMapping("/admin/rate/reset/{recipeId}")
    public String resetRate(@PathVariable String recipeId) {
        Recipe recipe = recipeService.getRecipeById(Long.parseLong(recipeId));
        List<Rating> ratings = ratingRepository.findByRecipe(recipe);
        ratingRepository.deleteAll(ratings);
        return "redirect:/recipe/" + recipeId;
    }
}
