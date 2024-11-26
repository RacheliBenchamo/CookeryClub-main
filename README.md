
# Cookery Club - Recipes Application
## Author
* Racheli Benchamo 

## Overview

This is a recipe sharing application built with Spring and Thymeleaf. The application allows users to share their recipes, comment on other recipes, rate them, and search through them. It also includes an admin user with extra privileges.

## Features

1. **Recipe Management:** Users can add, edit, and update their own recipes.
2. **Commenting:** Users can add comments to recipes, and delete their own comments.
3. **Rating:** Users can rate other users' recipes.
4. **Searching:** All users can search for recipes by the recipe name and the name of the recipe creator.
5. **Filtering:** Recipes can be filtered by their difficulty level, preparation time, and category.

## Search & Filter
![Search](/src/main/resources/static/images/search.png)

## Rating
![Rating](/src/main/resources/static/images/rating.png)

## Commenting
![Commenting](/src/main/resources/static/images/commenting.png)

## User Types & Permissions
![Login](/src/main/resources/static/images/login.png)

### Regular Users
Regular users have the ability to create, edit, and delete their own recipes. They can comment on any recipe and also delete their own comments. They can rate other users' recipes as well. Pre-registered users in the system are `Racheli`, `Geula`, `Yarin`, `Solange`, `Avigail` and their passwords are the same as their usernames.

### Admin User
The admin user has extended privileges over regular users. They can delete any recipe, delete all comments from a particular recipe, delete individual comments from any recipe, and reset the ratings of any recipe. The admin user credentials are:

- Username: `Admin`
- Password: `Admin`

### Visitors
Visitors, or non-registered users, have read-only access to the application. They can view the home page with all the recipes, and they can search for recipes by the recipe name or the name of the recipe creator. They can also filter recipes based on the difficulty level, preparation time, and category. However, to perform any other action, they will be redirected to the login page.

## Technology Stack

- **SpringBoot MVC:** Application logic is handled server-side using SpringBoot MVC.
- **Thymeleaf:** Thymeleaf is used as the view engine.
- **Spring Data JPA:** Data persistence is handled using Spring Data JPA with a MySQL database.
- **Spring Security:** Spring Security manages the user system, controlling permissions for users, admins, and visitors.
- **Bootstrap & CSS:** The frontend is built using Bootstrap and CSS to provide a responsive and user-friendly interface.

## Database Schema

The application uses the following tables:

1. **Recipes**
2. **Ingredient**
3. **Comment**
4. **Rating**

The tables are connected as follows:

- **Recipe** has a one-to-many relationship with **Ingredient**, **Rating** and **Comment**.
- **Rating** has a many-to-one relationship with **Recipe**.
- **Comment** has a many-to-one relationship with **Recipe**.
- **Ingredient** has a many-to-one relationship with **Recipe**.
- **Recipe** has many Categories.

#### Recipe Attributes:
- **id:** The recipe ID.
- **name:** The recipe name.
- **difficulty:** The recipe difficulty level.
- **preparationTime:** The recipe preparation time in minutes.
- **image:** The URL of the recipe image.
- **ingredients:** A list of the recipe ingredients.
- **instructions:** A list of the recipe preparation instructions.
- **categories:** A list of the recipe categories.
- **comments:** A list of the recipe comments.
- **ratings:** A list of the recipe ratings.
- **author:** The user who created the recipe.

#### Ingredient Attributes:
- **id:** The ingredient ID.
- **name:** The ingredient name.
- **quantity:** The ingredient quantity.
- **unit:** The ingredient unit.
- **recipe:** The recipe the ingredient belongs to.

#### Comment Attributes:
- **id:** The comment ID.
- **text:** The comment text.
- **recipe:** The recipe the comment belongs to.

#### Rating Attributes:
- **id:** The rating ID.
- **value:** The rating value.
- **recipe:** The recipe the rating belongs to.
- **user:** The user who rated the recipe.

## Navigation

The application includes a navigation bar (NAVBAR) for easy flow and access between different pages. Users can view their own recipes and the count of their recipes from any page.
#### Regular User Navigation Bar:
![Regular User Navigation Bar](/src/main/resources/static/images/regularUserNavBar.png)

#### Admin User Navigation Bar:
![Admin User Navigation Bar](/src/main/resources/static/images/adminNavBar.png)

## Error Handling

The application uses a GLOBAL EXCEPTION HANDLER for handling exceptions server-side. Any improper user actions lead to a custom error page.

## Initialization

On starting the application, six static recipes are loaded from a JSON file into the application using a service.
![Initialization Recipes](/src/main/resources/static/images/initializationRecipes.png)

## Instructions for Use

### Running the Site

On running the site, the user sees six static recipes. Non-registered users can see the home page with all the recipes, but for any other action, they will be redirected to the login page.

### Adding a New Recipe

A registered user can add a new recipe by clicking on the "Add Recipe" button on the home page and following these steps:

1. Enter the recipe name, choose a difficulty level, select categories, enter preparation time in minutes, and add a URL for a suitable image. After this step, the recipe will already exist in the system.
![Add Recipe](/src/main/resources/static/images/addRecipe.png)

   
2. Add ingredients needed for the recipe. Once at least one ingredient is added, the user can see the button to add preparation instructions.
![Add Ingredients](/src/main/resources/static/images/addIngredients.png)


3. Add preparation instructions.
![Add Instructions](/src/main/resources/static/images/addInstructions.png)


### Editing a Recipe

A user can edit their own recipe by clicking on the "view recipe" button then clicking the "edit" button.
The editing process is similar to the recipe creation process.
Editing ingredients allows the user to add, delete, and update ingredients.

# initialize the project
- create a new table in mysql with the name "ex5"
- start the mysql server
- run the project
- go to http://localhost:8080
- enjoy!😀


