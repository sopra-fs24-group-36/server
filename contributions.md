## Description
- manually append the two development tasks they completed during the week
- entry should consist of the date, your name, links to the github issues you worked on and optionally a short description

## Week 5
### Sarina 

### Jazz 
- 30.04, [#76](https://github.com/sopra-fs24-group-36/client/issues/76) implemented so ingredients from API are automatically added to recipes. We wanted to implement for instructions too but the API didnt provide instructions despite it being in the documentation.
- 30.04, [#73](https://github.com/sopra-fs24-group-36/client/issues/73) added a personal cookbook and a home button to the dashboard to make user interaction a bit easier.
- 30.04, [#72](https://github.com/sopra-fs24-group-36/client/issues/72) broccoli spinner nicely displays full broccolis
- 30.04, [#70](https://github.com/sopra-fs24-group-36/client/issues/70) made sure no empty fields can be added when creating or editing a recipe
- 30.04, [#71](https://github.com/sopra-fs24-group-36/client/issues/71) made sure recipes on home page are displayed as the same size like in the cookbooks. 

### Yujie

### Summer 

### Marko 
- 01.05, [#117](https://github.com/sopra-fs24-group-36/client/issues/117) Implemented a Database for Calendars.
- 01.05, [#114](https://github.com/sopra-fs24-group-36/client/issues/114) Added the POST request for a user calendar by adding the necessary entities and mappers.
- 01.05, [#119](https://github.com/sopra-fs24-group-36/client/issues/119) Added the POST request for a group calendar.
- 02.05, [#115](https://github.com/sopra-fs24-group-36/client/issues/115) Added the DELETE request for a user calendar but had to change the post request a bit so it works better.
- 02.05, [#120](https://github.com/sopra-fs24-group-36/client/issues/120) Added the DELETE request for a group calendar.
- 02.05, [#143](https://github.com/sopra-fs24-group-36/client/issues/143) A random cooking-fact can now be requested with a new GET request.
- 03.05, [#118](https://github.com/sopra-fs24-group-36/client/issues/118) Added the GET request for a user calendar but had to change the post and delete request a bit so it works better.
- 03.05, [#113](https://github.com/sopra-fs24-group-36/client/issues/113) Added the GET request for a group calendar.

## Week 4
### Sarina 
- deployed the persistence of database tables to google cloud (with almost no issues)
- 24.04, [#10](https://github.com/sopra-fs24-group-36/server/issues/10), fixed the remove method for this since it got several logic errors and cause inconsistencies in the database (commit #63)
- 25.04, [#56](https://github.com/sopra-fs24-group-36/server/issues/56) and [#55](https://github.com/sopra-fs24-group-36/server/issues/55), needed to change those methods completely since they cause unexpected behavior in the database and in the frontend, also needed to rewrite all the tests (commit #67)

### Jazz 
- 24.04, [#36](https://github.com/sopra-fs24-group-36/client/issues/36) made sure the API is connected to the backend - is working
- 25.04, [#15](https://github.com/sopra-fs24-group-36/client/issues/15) implemented all guards to make sure the user can only view the correct content 

### Yujie
- 23.04, [#46](https://github.com/sopra-fs24-group-36/client/issues/46) implemented deleting/removing recipes from cookbook
- 23.04, [#47](https://github.com/sopra-fs24-group-36/client/issues/47) finalize all image-related functions

### Summer 
- 23.04, [#29](https://github.com/sopra-fs24-group-36/client/issues/29) Make that all members of a group can edit the group shopping list simultaneously.
- 24.04,  [#7](https://github.com/sopra-fs24-group-36/client/issues/7) Create a Spinner Object (brokkoli) to display while users are waiting (optional)

### Marko 
- 23.04, a lot of bug fixes and also test writing.
- 24.04, [#56](https://github.com/sopra-fs24-group-36/server/issues/56) and [#55](https://github.com/sopra-fs24-group-36/server/issues/55) needed to change the handling of invitations from the group side because of bugs in the database. Also had to change the already existing tests to accomodate for the changes.
- 25.04, [#73](https://github.com/sopra-fs24-group-36/server/issues/73) and [#48](https://github.com/sopra-fs24-group-36/server/issues/48), The creation of a group caused a lot of problems due to bugs in the backend which lead to inconsistencies in the database. Had to rewrite all tests and write new ones to accomodate new exceptions.
- 26.04, smaller bug fixes and preparing the necessary tests for the report.

## Week 3

### Sarina
- 17.04, [#29](https://github.com/sopra-fs24-group-36/server/issues/29) and [#89](https://github.com/sopra-fs24-group-36/server/issues/89), added the two REST endpoints to the systems including the tests
- 20.04, regarding all issues involving groups, I added all the tests for all the group REST endpoints
- 22.04, [#72](https://github.com/sopra-fs24-group-36/server/issues/72), finished the persistence of the server 

### Jazz
- 16.04, [#31](https://github.com/sopra-fs24-group-36/client/issues/31) the 3 most recently added recipes are shown on the home page, if the user has no recipes "no recipes yet" is displayed 
- 18.04, [#23](https://github.com/sopra-fs24-group-36/client/issues/23) the user can only edit their own recipes
- 19.04, [#17](https://github.com/sopra-fs24-group-36/client/issues/17) the groups a user is part of are displayed on the home page. if the user is not part of any groups, a message is shown.
- 22.04, [#24](https://github.com/sopra-fs24-group-36/client/issues/24) recipes can be added to new groups via edit after creation. edited content is displayed in all cookbooks the recipe is part of.
- 22.04, [#20](https://github.com/sopra-fs24-group-36/client/issues/20) UI and functionalities implemented to add a recipe to a group on recipe creation.
- 22.04, [#36](https://github.com/sopra-fs24-group-36/client/issues/36) connected the API - just need to check uploading to github with Marion
- 22.04, [#31](https://github.com/sopra-fs24-group-36/client/issues/31) all functionalities & buttons on the home page work
- 22.04, [#39](https://github.com/sopra-fs24-group-36/client/issues/39) group functionalites for recipes - this issues was completed with issues #24 and #17


### Yujie
- 21.04, [#33](https://github.com/sopra-fs24-group-36/client/issues/33), connect group cookbook to backend, it can display recipes in it
- 21.04, [#34](https://github.com/sopra-fs24-group-36/client/issues/34), connect personal cookbook to backend, it can display recipes in it
- 21.04, [#35](https://github.com/sopra-fs24-group-36/client/issues/35), connect user profile and edit profile to backend, the information changed is saved to the server, add authentication, user can only edit their own profile
- 21.04, [#44](https://github.com/sopra-fs24-group-36/client/issues/44), connect personal shoppinglist to backend, we can add, remove and clear all items
- 21.04, [#45](https://github.com/sopra-fs24-group-36/client/issues/45), connect group shoppinglist to backend, we can add, remove and clear all items
  
### Summer
- 21.04,[#37](https://github.com/sopra-fs24-group-36/client/issues/37) Create a calendar view of a user
- 21.04,[#38](https://github.com/sopra-fs24-group-36/client/issues/37) create a calendar view of a group
- 22.04,[#32](https://github.com/sopra-fs24-group-36/client/issues/37) make the invitation functionalities connected to backend

### Marko
- 15.04, [#43](https://github.com/sopra-fs24-group-36/client/issues/43), was already implemented, had to do some bug fixes.
- 16.04, [#39](https://github.com/sopra-fs24-group-36/client/issues/39), worked on the rest-specs and implemented all needed tests.
- 17.04, [#48](https://github.com/sopra-fs24-group-36/client/issues/48), rest specifications, bug fixes and all needed tests done.
- 18.04, [#56](https://github.com/sopra-fs24-group-36/client/issues/56), had to change some REST-endpoints so invitations are done by the user. Added all needed tests.
- 19.04, [#66](https://github.com/sopra-fs24-group-36/client/issues/66), Rest specs and a ton of tests.
- 19.04, [#55](https://github.com/sopra-fs24-group-36/client/issues/55), rest specs and tests.
- 19.04, [#50](https://github.com/sopra-fs24-group-36/client/issues/50), had to add a new undefined rest-endpoint, tested it also.
- 20.04, Wrote a loooooot of tests for most Controllers, Repositories, IntegrationTests, Services, Mappers regarding Recipes, Cookbooks, ShoppingLists and some for new Group-related requests.
- 21.04, Continued with writing all needed tests for the issues above, deleted some unused files and tried to boost up the Test-coverage.

## Week 2

### Sarina
- 15.04, [#23](https://github.com/sopra-fs24-group-36/server/issues/23), added all tests
- 15.04, [#37](https://github.com/sopra-fs24-group-36/server/issues/37), added all tests
- 12.04, [#71](https://github.com/sopra-fs24-group-36/server/issues/71), agreed on using postgres database to store and persist our data
- 15.04, [#72](https://github.com/sopra-fs24-group-36/server/issues/72), after various tries to get a working and correct solution i found one (having two databases, one for tests and one for the rest, while only the one not for tests actually persists data since it is not needed for the tests and makes things complicated)

### Jazz
- 09.04, [#25](https://github.com/sopra-fs24-group-36/client/issues/25), [#4](https://github.com/sopra-fs24-group-36/client/issues/4), finished UI of search bar, made username in top corner clickable, a few other minor changes
- 12.04, [#30](https://github.com/sopra-fs24-group-36/client/issues/30), working on connection to backend - few changes to recipe page & making sure login and register work
- 13.04, [#30](https://github.com/sopra-fs24-group-36/client/issues/30), working on connection to backend - recipes are successfully saved to database
- 14.04, [#11](https://github.com/sopra-fs24-group-36/client/issues/11), working on connection to backend - recipe page is displayed correctly & viewing recipe page finished
- 15.04, [#22](https://github.com/sopra-fs24-group-36/client/issues/22), created recipe editing view which is connected to backend. Groups and images not finalised yet as they were not planned for this week (new issue opened). Also completed [#14](https://github.com/sopra-fs24-group-36/client/issues/14) in the process. 

### Yujie
- 09.04, [#16](https://github.com/sopra-fs24-group-36/client/issues/16), created a group creation view
- 11.04, [#19](https://github.com/sopra-fs24-group-36/client/issues/19), created a invitation view. Fixed dashboard, made it display different buttons depending on different pages
- 12.04, [#26](https://github.com/sopra-fs24-group-36/client/issues/26), created a shopping list view

### Summer
- 14.04 [#21](https://github.com/sopra-fs24-group-36/client/issues/21), Create a Invitations page
- 15.04 [#12](https://github.com/sopra-fs24-group-36/client/issues/12), create a search bar to search by tags or names

### Marko
- 13.04, Marko, [#36](https://github.com/sopra-fs24-group-36/client/issues/36) added the post/get requests to store single recipes in the database, as well es retrieve single/multiple ones. 
- 14.04, Marko, [#59](https://github.com/sopra-fs24-group-36/client/issues/59) added the put requests to update single recipes in the database. 
- 15.04 Marko, [#73](https://github.com/sopra-fs24-group-36/client/issues/73) added all necessary things for the recipes including delete.




## Week 1 (including easter break)

### Sarina
- 30.03, Sarina, [#30](https://github.com/sopra-fs24-group-36/server/issues/30), added the whole setup for the USER (User Entity, Repository, Service, etc.)  
- 30.03, Sarina, [#21](https://github.com/sopra-fs24-group-36/server/issues/21), to reason about whether the setup for the USER database was correct I implemented a basic scenario and added tests for this    
- 01.04, Sarina, [#31](https://github.com/sopra-fs24-group-36/server/issues/31), created the database table for COOKBOOKS (Entity, Repository, Service, etc.)    
- 05.04, Sarina, [#23](https://github.com/sopra-fs24-group-36/server/issues/23) and [# 37](https://github.com/sopra-fs24-group-36/server/issues/37), completed the functionality but still need to add some tests (hence not displayed as done yet)     

### Jazz
- 26.03. Jasmine, (concerns many issues among them [#25](https://github.com/sopra-fs24-group-36/client/issues/25)), created base components present on numerous pages: Dashboard, Header, Footer, new base container
- 27.03 & 28.03, Jasmine, [#3](https://github.com/sopra-fs24-group-36/client/issues/3), created homepage view based on Figma design
- 30.03, Jasmine, [#9](https://github.com/sopra-fs24-group-36/client/issues/9), created the page to add a recipe
- 06.04, Jasmine, [#9](https://github.com/sopra-fs24-group-36/client/issues/9) & [#3](https://github.com/sopra-fs24-group-36/client/issues/3), added pictures, some minor styling on homepage and recipe adding page
- 07.04, Jasmine, [#11](https://github.com/sopra-fs24-group-36/client/issues/11), created basic UI components for viewing a recipe (not complete) 

### Yujie
- 31.03, Yujie, [#1](https://github.com/sopra-fs24-group-36/client/issues/1), Created the login page    
- 31.03, Yujie, [#2](https://github.com/sopra-fs24-group-36/client/issues/2), Created the register page, edit Router    
- 07.04, Yujie, [#18](https://github.com/sopra-fs24-group-36/client/issues/18), Created group cookbook view, changed #10 personal cookbook view    


### Summer
- 01.04, xiaying, [#5](https://github.com/sopra-fs24-group-36/client/issues/5), created the profile page   
- 02.04, Xiaying, [#6](https://github.com/sopra-fs24-group-36/client/issues/6), created the editing profile page   
- 02.04, xiaying, [#10](https://github.com/sopra-fs24-group-36/client/issues/10), created the personalCookbook page    

### Marko
- 04.04, Marko, [#34](https://github.com/sopra-fs24-group-36/server/issues/34), added the setup for the Recipe Database (Recipe Entity, Repository, Service, ...)
- 05.04, Marko, [#47](https://github.com/sopra-fs24-group-36/server/issues/47), added the same setup for the Group Database (Group Entity, Repository, Service, ...)
- 05.04, Marko, [#66](https://github.com/sopra-fs24-group-36/server/issues/66), setup the database for ShoppingLists (ShoppingList Entity, Repository, Service, ...)
- 07.04, Marko, [#47](https://github.com/sopra-fs24-group-36/server/issues/47), the invitations are stored with the user and not the group, changed it.
- 08.04, Marko, [#66](https://github.com/sopra-fs24-group-36/server/issues/66) and [#47](https://github.com/sopra-fs24-group-36/server/issues/47), added to the DTOMapper all necessary mappers for groups/shoppingLists/recipes
- 08.04, Marko, [#66](https://github.com/sopra-fs24-group-36/server/issues/66) and [#47](https://github.com/sopra-fs24-group-36/server/issues/47), added to the DTOMapper all necessary mappers for groups/shoppingLists/recipes
