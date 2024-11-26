package hac.controllers.UserControllers;

import hac.services.RecipeService;
import org.springframework.ui.Model;
import hac.repo.comment.Comment;
import hac.repo.comment.CommentRepository;
import hac.repo.recipe.Recipe;
import hac.repo.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserCommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RecipeService recipeService;

    // Handles the POST request to add a comment for a specific recipe
    @PostMapping("/user/comment/{recipeId}")
    public String addComment(@RequestParam("comment") String comment, @PathVariable String recipeId, Model model) {
        Recipe recipe = recipeService.getRecipeById(Long.parseLong(recipeId));

        if(comment.trim().isEmpty()){
            return "redirect:/recipe/" + recipeId + "?error=true";
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        Comment comm = new Comment(comment, username, recipe);
        commentRepository.save(comm);

        return "redirect:/recipe/" + recipeId;
    }
}
