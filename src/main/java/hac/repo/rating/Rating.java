package hac.repo.rating;

import hac.repo.recipe.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

// This class is used to create a rating object
@Entity
public class Rating implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull(message = "Rate is mandatory")
        private Integer rate;

        @NotEmpty(message = "User name is mandatory")
        @NotBlank(message = "User name cannot be blank")
        private String userName;

        @ManyToOne
        @JoinColumn(name = "recipeId")
        private Recipe recipe;

        public Rating(Integer rate, String userName, Recipe recipe) {
                this.rate = rate;
                this.userName = userName;
                this.recipe = recipe;
        }

        public Rating() {}

        public Long getId() { return id; }
        public Integer getRate() { return rate; }
        public String getUserName() { return userName; }
        public Recipe getRecipe() { return recipe; }
        public void setId(Long id) { this.id = id; }
        public void setRate(Integer rate) { this.rate = rate; }
        public void setUserName(String userName) { this.userName = userName; }
        public void setRecipe(Recipe recipe) { this.recipe = recipe; }

}




