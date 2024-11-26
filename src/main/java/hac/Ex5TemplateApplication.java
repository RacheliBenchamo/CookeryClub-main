package hac;

import hac.services.RecipeService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Ex5TemplateApplication {

    @Autowired
    private RecipeService recipeService;

    private final String filePath = "src/main/resources/static/predefinedRecipes.json";


    public static void main(String[] args) {

        SpringApplication.run(Ex5TemplateApplication.class, args);
    }

    @PostConstruct
    public void init() {
        try {
            recipeService.loadRecipesFromJsonFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load recipes from file");
        }
    }

}
