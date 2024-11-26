package hac.repo.recipe;

import hac.repo.comment.Comment;
import hac.repo.ingredient.Ingredient;
import hac.repo.rating.Rating;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import java.util.List;

// This class is used to create a recipe object
@Entity
public class Recipe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @NotEmpty(message = "Recipe name is mandatory")
    @NotBlank(message = "Recipe name cannot be blank")
    private String recipeName;

    @ElementCollection
    @NotEmpty(message = "Recipe category is mandatory")
    private List<String> category;

    @URL(message = "Recipe picture must be a valid URL")
    @NotEmpty(message = "Recipe picture is mandatory")
    private String picture;

    @NotEmpty(message = "Recipe author is mandatory")
    @NotBlank(message = "Recipe author cannot be blank")
    private String author;

    @NotEmpty(message = "Recipe difficulty is mandatory")
    private String difficulty;

    @NotNull(message = "Recipe time is mandatory")
    @PositiveOrZero(message = "Recipe time must be positive or zero")
    private Integer time;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipeId")
    private List<Ingredient> ingredients;

    @Column(columnDefinition = "TEXT")
    private String instructions;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipeId")
    private List<Rating> ratings;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipeId")
    private List<Comment> comments;

    // Double rating, Integer num_ratings,
    public Recipe(String recipeName,List<Ingredient> ingredients, String instructions, List<String> category, String picture, String author,
                  String difficulty, Integer time, List<Rating> ratings, List<Comment> comments) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.picture = picture;
        this.author = author;
        this.difficulty = difficulty;
        this.time = time;
        this.ratings = ratings;
        this.comments = comments;
    }
    public Recipe() {}
    public Long getRecipeId() { return recipeId; }
    public String getRecipeName() { return recipeName; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public List<String> getCategory() { return category; }
    public String getPicture() { return picture; }
    public String getAuthor() { return author; }
    public String getDifficulty() { return difficulty; }
    public Integer getTime() { return time; }
    public List<Rating> getRatings() { return ratings; }
    public List<Comment> getComments() { return comments; }
    public void setRecipeId(Long recipeId) { this.recipeId = recipeId; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setCategory(List<String> category) { this.category = category; }
    public void setPicture(String picture) { this.picture = picture; }
    public void setAuthor(String author) { this.author = author; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setTime(Integer time) { this.time = time; }
    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}


