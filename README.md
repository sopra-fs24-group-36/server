# 🥦 Virtual Cookbook 🥦
Save recipes, find inspiration and plan for future meals. Your cooking dilemmas solved with the virtual cookbook. 
The frontend implementation can be found [here](https://github.com/sopra-fs24-group-36/client). 

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
We are confronted with one of life's most difficult decisions every day, what to eat? Living alone we quickly run out of inspiration. Living with friends or family leads to discussions and disagreements on what to eat. Often we receive a recipe recommendation, but that gets lost or forgotten. Often we turn to the internet for inspiration, but how do we navigate the endless information? 

With the virtual cookbook these problems are solved. Save your personal recipes to a personal cookbook. Create groups with whoever you live with and add recipes to the group cookbook. Use the calendar for meal planning and the shopping list to keep track of items you need. And if you ever run out of inspiration, the built in search-function helps you find new recipes which can directly be saved. 

## 🥦 Technologies 
The following technologies were used for back-end development: 
- Java: programming language
- [Gradle](https://gradle.org/): build automation tool
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Google Cloud](https://cloud.google.com/?hl=en): for deployment
- [PostgreSQL](https://cloud.google.com/sql/docs/postgres) from Google Cloud: for data persistance storing recipes, user profiles and more


## 🥦 High-level components 

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
Please ensure both client and server are running for the application to work. Also note that the persistant server... 

### Releases 
???

## 🥦 Roadmap 
- *AI extension*: add an AI feature which allows recipe images to optionally be created by AI

## 🥦 Authors
- [Marko Cerkez](https://github.com/markocerkez) - server
- [Jasmine Rose Chapman](https://github.com/jazzyywazzyy) - client
- [Sarina Alessandra Gmünder](https://github.com/markocerkez) - server
- [Yujie Han](https://github.com/JadeHan1127) - client
- [Xiaying Ji](https://github.com/shalynjjj) - client

## 🥦 Acknowledgements 
Many thanks to our teaching assistant [Marion Andermatt](https://github.com/marion-an) for her help, support and guidance during this project. 

## 🥦 License



