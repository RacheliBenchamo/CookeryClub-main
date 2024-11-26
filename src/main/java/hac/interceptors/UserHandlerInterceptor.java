package hac.interceptors;

import hac.repo.recipe.Recipe;
import hac.repo.recipe.RecipeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component
public class UserHandlerInterceptor implements HandlerInterceptor {
    private final RecipeRepository recipeRepository;

    // Constructor
    @Autowired
    public UserHandlerInterceptor(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // This method is called before the controller methods are executed
    // it adds the user's username and the number of recipes they have to the request
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            request.setAttribute("user", username);
            request.setAttribute("userRecipes", getUserRecipes(username).size());
        }
        return true;
    }

    // Returns the recipes that the user has created
    private List<Recipe> getUserRecipes(String username) {
        return recipeRepository.findByAuthor(username);
    }
}
