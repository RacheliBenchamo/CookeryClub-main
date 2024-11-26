package hac.controllers;

import hac.repo.comment.Comment;
import hac.repo.comment.CommentRepository;
import hac.repo.recipe.Recipe;
import hac.repo.recipe.RecipeRepository;
import hac.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SharedController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    // Deletes a recipe with the given recipeId
    @PostMapping("/shared/delete/recipe/{recipeId}")
    public String deleteRecipe(@PathVariable String recipeId) {
        Recipe recipe = recipeService.getRecipeById(Long.parseLong(recipeId));
        recipeRepository.delete(recipe);
        return "redirect:/";
    }

    // Handles the Post request to display the add comment page for a specific recipe
    @PostMapping("/shared/comment/delete/{commentId}")
    public String deleteComment(@PathVariable String commentId) {
        Comment comment = commentRepository.findById(Long.parseLong(commentId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + commentId));
        commentRepository.delete(comment);
        return "redirect:/recipe/" + comment.getRecipe().getRecipeId();
    }
}
