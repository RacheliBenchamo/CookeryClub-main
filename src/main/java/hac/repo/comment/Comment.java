package hac.repo.comment;

import hac.repo.recipe.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

// This class is used to create a comment object
@Entity
public class Comment implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty(message = "Comment is mandatory")
        @NotBlank(message = "Comment cannot be blank")
        private String comment;

        @NotEmpty(message = "User name is mandatory")
        @NotBlank(message = "User name cannot be blank")
        private String userName;

        @ManyToOne
        @JoinColumn(name = "recipeId")
        private Recipe recipe;

        public Comment(String comment, String userName, Recipe recipe) {
                this.comment = comment;
                this.userName = userName;
                this.recipe = recipe;
        }

        public Comment() {}
        public Long getId() { return id; }
        public String getComment() { return comment; }
        public String getUserName() { return userName; }
        public Recipe getRecipe() { return recipe; }
        public void setId(Long id) { this.id = id; }
        public void setRate(String comment) { this.comment = comment; }
        public void setUserName(String userName) { this.userName = userName; }
        public void setRecipe(Recipe recipe) { this.recipe = recipe; }

}




