package hac.repo.recipe;

import hac.repo.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthor(String username);

    List<Recipe> findByAuthorContainingIgnoreCase(String keyword);

    List<Recipe> findByRecipeNameContainingIgnoreCase(String keyword);

    List<Recipe> findByCategoryContainingIgnoreCase(String category);

    List<Recipe> findByTimeBetween(Integer minCookingTime, Integer maxCookingTime);

    List<Recipe> findByDifficultyContainingIgnoreCase(String maxDifficulty);
}
