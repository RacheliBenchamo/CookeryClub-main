package hac.repo.ingredient;

import hac.repo.recipe.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;

// This class is used to create an ingredient object
@Entity
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Ingredient name is mandatory")
    @NotBlank(message = "Ingredient name cannot be blank")
    private String name;

    @NotNull(message = "Ingredient quantity is mandatory")
    @Positive(message = "Ingredient quantity must be positive")
    private Double quantity ;

    @NotEmpty(message = "Ingredient unit is mandatory")
    @NotBlank(message = "Ingredient unit cannot be blank")
    private String unit;

    @ManyToOne
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    public Ingredient(String name, Double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Ingredient() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public Recipe getRecipe() { return recipe; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}




