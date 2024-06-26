# 🥦 Virtual Cookbook 🥦
Save recipes, find inspiration and plan for future meals. Your cooking dilemmas solved with the virtual cookbook. 
Click [here](https://github.com/sopra-fs24-group-36/client) to find the frontend implementation. 

## 🥦 Table of contents 
1. [Introduction](#introduction) 
2. [Technologies](#technologies)
3. [High-level components](#high-level-components)
4. [Launch and Deployment](#launch-and-deployment)
5. [Roadmap](#roadmap)
6. [Authors](#authors)
7. [Acknowledgements](#acknowledgements)
8. [License](#license)

## 🥦 Introduction
Every day we are confronted with one of life's most difficult decisions, what to eat? Living alone we quickly run out of ideas. If we live with friends or family cooking always leads to discussions and disagreements on what to eat. We receive recipe recommendations, but they get lost or forgotten. Often we turn to the internet for inspiration, but how do we navigate the endless information? 

With the virtual cookbook these problems are solved. Save your personal recipes to a personal cookbook. Create groups with your household and add recipes to your group cookbook. Use the calendar for meal planning and the shopping list to keep track of items you need. And if you ever run out of inspiration, the built in search-function helps you find new recipes which can directly be saved.

## 🥦 Technologies 
The following technologies were used for back-end development: 
- Java: programming language
- [Gradle](https://gradle.org/): build automation tool
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Google Cloud](https://cloud.google.com/?hl=en): for deployment
- [PostgreSQL](https://cloud.google.com/sql/docs/postgres) from Google Cloud: for data persistance storing recipes, user profiles and more


## 🥦 High-level components
In general, the REST requests are handled within the Controller classes, while the application logic handling is managed by the various Service classes. Data persistence is handled by the Repository classes, and the project includes various Entity classes representing the whole data model. 
The following sections provide a detailed description of our application's key components.

### 🍳 RecipeService
The [RecipeService](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/RecipeService.java) is responsible for managing all recipe-related operations. This includes the creation of a new recipe, updating existing recipes, retrieving recipe details, and the deletion of recipes. The RecipeService interacts with the [RecipeRepository](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/repository/RecipeRepository.java), which communicates with the database to persist recipe data. The service ensures that users can manage their recipes efficiently. The RecipeService is a central component for users who want to save and share their cooked creations.

### 👫 GroupService
The [GroupService](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/GroupService.java) is dedicated to managing all aspects of user groups within the application. This includes functionalities such as creating new groups, adding or removing members, and deleting groups. It works closely with the [GroupRepository](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/repository/GroupRepository.java) to ensure group data is accurately stored and retrieved. The GroupService allows users to create collaborative communities dedicated to cooking and recipe sharing and meal planning. It facilitates shared shopping lists and coordinated meal planning, making it a crucial element for fostering community interaction and enhancing social engagement within the app.

### 🧩 DTO and Mapper
The DTO (Data Transfer Object) classes are used to transfer all necessary data between the client and server. The DTOMapper class is responsible for mapping between entity classes and their corresponding DTOs. This abstraction layer ensures that the data structures used in API communication are separate from the internal data model, enhancing maintainability and scalability.

### 📅 Calendar
The [Calendar class](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Calendar.java) is responsible for managing calendar entries, specifically linking dates to recipes. It includes an ID and a list of DateRecipe objects, which represent the recipes associated with specific dates and their Status using the enumeration from the [CalendarStatus](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/constant/CalendarStatus.java) constant. This class ensures that users can organise their cooking plans by assigning recipes to particular dates. The Calendar class interacts with the persistence layer to store and retrieve the necessary calendar data. It is a crucial component for users and groups who want to plan their meals and maintain an organised cooking calendar.

### 📝 ShoppingListController
The [ShoppingListController](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/controller/ShoppingListController.java) oversees all client-server interactions related to user- and group shopping lists. This includes handling requests to add, tick, and retrieve items from a shopping list. The controller ensures that any changes made to the shopping list are synchronised across all clients, providing a consistent experience. The controller works in collaboration with the [ShoppingListService](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/ShoppingListService.java) to execute the necessary business logic and interact with the ShoppingListRepository for data persistence. Through the [ShoppingListController](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/repository/ShoppingListRepository.java), users and groups can efficiently organise their grocery needs, ensuring their shopping lists are always up-to-date and accessible.

## 🥦 Launch and Deployment 
### Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

Verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode
Follow these steps to start the backend in development mode. Please note that a new build will be triggered automatically if the content of a file is changed. 

Open two terminal windows
Run this command in one window:

`./gradlew build --continuous`

and this command in the other window:

`./gradlew bootRun`

Use the following command if you want to avoid running the tests with every change:

`./gradlew build --continuous -xtest`

### Dependencies 
Please ensure both client and server are running for the application to work. Note that a Google Cloud account must be set up for the persistant server to function. Please create an account [here](https://cloud.google.com/sql/docs/postgres) and then your credentials to the [application properties](https://github.com/sopra-fs24-group-36/server/blob/main/src/main/resources/application.properties) file as *spring.datasource.username* and *spring.datasource.password*. 

## 🥦 Roadmap 
- *AI extension*: add an AI feature which allows recipe images to optionally be created by AI
- *Recipe Filter*: allow recipes to be filtered by rating in the group cookbooks. 
- *Group Chat*: create a group chat feature for the group cookbooks.

## 🥦 Authors
- [Marko Cerkez](https://github.com/markocerkez) - server
- [Jasmine Rose Chapman](https://github.com/jazzyywazzyy) - client
- [Sarina Alessandra Gmünder](https://github.com/markocerkez) - server
- [Yujie Han](https://github.com/JadeHan1127) - client
- [Xiaying Ji](https://github.com/shalynjjj) - client

## 🥦 Acknowledgements 
Many thanks to our teaching assistant [Marion Andermatt](https://github.com/marion-an) for her help, support and guidance during this project. :)

## 🥦 License
This project is licensed under the Apache License, Version 2.0, January 2004.



