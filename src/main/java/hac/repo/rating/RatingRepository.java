package hac.repo.rating;

import hac.repo.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRecipe(Recipe r);
    Rating findByUserNameAndRecipe(String username, Recipe r);
}
