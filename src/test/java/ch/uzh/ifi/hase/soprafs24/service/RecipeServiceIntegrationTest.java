package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import ch.uzh.ifi.hase.soprafs24.rest.dto.VotingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
public class RecipeServiceIntegrationTest {

    @Qualifier("groupRepository")
    @Autowired
    private GroupRepository groupRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("cookbookRepository")
    @Autowired
    private CookbookRepository cookbookRepository;

    @Qualifier("recipeRepository")
    @Autowired
    private RecipeRepository recipeRepository;

    @Qualifier("commentRepository")
    @Autowired
    private CommentRepository commentRepository;

    @Qualifier("calendarRepository")
    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        groupRepository.deleteAll();
        userRepository.deleteAll();
        recipeRepository.deleteAll();
        cookbookRepository.deleteAll();
        commentRepository.deleteAll();
    }


    //  test createGroup method //
    @Test
    public void createGroup_validInputs_success() {

      List<String> testlist = new ArrayList<>();
      testlist.add("test");

      List<RecipeTags> testtags = new ArrayList<>();
      testtags.add(RecipeTags.VEGAN);

      List<Long> testgroups = new ArrayList<>();

      Recipe testRecipe = new Recipe();
      testRecipe.setTitle("test");
      testRecipe.setShortDescription("test");
      testRecipe.setLink("test");
      testRecipe.setCookingTime("test");
      testRecipe.setAmounts(testlist);
      testRecipe.setIngredients(testlist);
      testRecipe.setInstructions(testlist);
      testRecipe.setImage("test");
      testRecipe.setTags(testtags);
      testRecipe.setGroups(testgroups);
      testRecipe.setAuthorID(1L);
      
      User user = new User();
      user.setPassword("test");
      user.setUsername("test");
      user.setEmail("test");
      user.setCreationDate(new Date());
      user.setStatus(UserStatus.ONLINE);
      user.setToken("test");
      Cookbook cookbook = new Cookbook();
      cookbook.setStatus(CookbookStatus.PERSONAL);
      cookbook.setRecipes(new ArrayList<>());
      user.setCookbook(cookbook);
      cookbookRepository.save(cookbook);
      userRepository.save(user);
      // when
      Recipe createdRecipe = recipeService.createUserRecipe(user.getId(), testRecipe);

      // then
      assertEquals(testRecipe.getId(), createdRecipe.getId());
    }

    @Test
    public void createGroupRecipe_validInputs_success() {

      List<String> testlist = new ArrayList<>();
      testlist.add("test");

      List<RecipeTags> testtags = new ArrayList<>();
      testtags.add(RecipeTags.VEGAN);

      List<Long> testgroups = new ArrayList<>();

      List<String> testmember = new ArrayList<>();

      Recipe testRecipe = new Recipe();
      testRecipe.setTitle("test");
      testRecipe.setShortDescription("test");
      testRecipe.setLink("test");
      testRecipe.setCookingTime("test");
      testRecipe.setAmounts(testlist);
      testRecipe.setIngredients(testlist);
      testRecipe.setInstructions(testlist);
      testRecipe.setImage("test");
      testRecipe.setTags(testtags);
      testRecipe.setGroups(testgroups);
      
      Cookbook cookbook = new Cookbook();
      cookbook.setRecipes(testgroups);
      cookbook.setStatus(CookbookStatus.GROUP);
      Group group = new Group();
      group.setName("test");
      group.setMembers(testgroups);
      group.setMembersNames(testmember);
      group.setCookbook(cookbook);
      group.setImage("test");
      cookbookRepository.save(cookbook);
      groupRepository.save(group);
      // when
      Recipe createdRecipe = recipeService.createGroupRecipe(group.getId(), testRecipe);

      // then
      assertEquals(testRecipe.getId(), createdRecipe.getId());
    }

    @Test
    public void findAllRecipesWithUserID_validInputs_success() {

      List<String> testlist = new ArrayList<>();
      testlist.add("test");

      List<RecipeTags> testtags = new ArrayList<>();
      testtags.add(RecipeTags.VEGAN);

      List<Long> testgroups = new ArrayList<>();

      Recipe testRecipe = new Recipe();
      testRecipe.setTitle("test");
      testRecipe.setShortDescription("test");
      testRecipe.setLink("test");
      testRecipe.setCookingTime("test");
      testRecipe.setAmounts(testlist);
      testRecipe.setIngredients(testlist);
      testRecipe.setInstructions(testlist);
      testRecipe.setImage("test");
      testRecipe.setTags(testtags);
      testRecipe.setGroups(testgroups);
      testRecipe.setAuthorID(1L);

      recipeRepository.save(testRecipe);

      // when
      List<Recipe> recipelist = recipeService.findAllRecipesWithUserID(1L);

      // then
      assertNotNull(recipelist);
    }

    @Test
    public void findRecipeById_validInputs_success() {

      List<String> testlist = new ArrayList<>();
      testlist.add("test");

      List<RecipeTags> testtags = new ArrayList<>();
      testtags.add(RecipeTags.VEGAN);

      List<Long> testgroups = new ArrayList<>();

      Recipe testRecipe = new Recipe();
      testRecipe.setTitle("test");
      testRecipe.setShortDescription("test");
      testRecipe.setLink("test");
      testRecipe.setCookingTime("test");
      testRecipe.setAmounts(testlist);
      testRecipe.setIngredients(testlist);
      testRecipe.setInstructions(testlist);
      testRecipe.setImage("test");
      testRecipe.setTags(testtags);
      testRecipe.setGroups(testgroups);
      testRecipe.setAuthorID(1L);

      recipeRepository.save(testRecipe);

      // when
      Recipe returnrecipe = recipeService.findRecipeById(testRecipe.getId());

      // then
      assertNotNull(returnrecipe);
    }

    @Transactional 
    @Test
    public void updateRecipe_validInputs_success() {

      // Existing recipe setup
      List<String> oldlist = new ArrayList<>();
      oldlist.add("test");
      oldlist.add("test2");

      List<RecipeTags> oldtags = new ArrayList<>();
      oldtags.add(RecipeTags.VEGAN);

      Recipe oldRecipe = new Recipe();
      oldRecipe.setTitle("test");
      oldRecipe.setShortDescription("test");
      oldRecipe.setLink("test");
      oldRecipe.setCookingTime("test");
      oldRecipe.setAmounts(oldlist);
      oldRecipe.setIngredients(oldlist);
      oldRecipe.setInstructions(oldlist);
      oldRecipe.setImage("test");
      oldRecipe.setTags(oldtags);
      oldRecipe.setAuthorID(1L);

      // Saving the recipe
      recipeRepository.save(oldRecipe);

      // Updated version of recipe setup
      List<String> changedList = new ArrayList<>();
      changedList.add("testupdate");
      changedList.add("testupdate2");

      List<RecipeTags> changedTags = new ArrayList<>();
      changedTags.add(RecipeTags.VEGETARIAN);

      Recipe changedRecipe = new Recipe();
      changedRecipe.setTitle("testupdate");
      changedRecipe.setShortDescription("testupdate");
      changedRecipe.setLink("testupdate");
      changedRecipe.setCookingTime("testupdate");
      changedRecipe.setAmounts(changedList);
      changedRecipe.setIngredients(changedList);
      changedRecipe.setInstructions(changedList);
      changedRecipe.setImage("testupdate");
      changedRecipe.setTags(changedTags);

      // update the existing recipe with the existing-recipes ID and an updated version of the recipe
      recipeService.updateRecipe(oldRecipe.getId(), changedRecipe);

      // Retrieve updated Recipe
      Recipe newTestRecipe = recipeRepository.findById(oldRecipe.getId()).orElse(null);

      // check that the updated stuff is not the same since we changed everything
      assertNotNull(newTestRecipe);
      assertEquals(newTestRecipe.getId(), oldRecipe.getId());
      assertEquals(newTestRecipe.getTitle(), changedRecipe.getTitle());
      assertEquals(newTestRecipe.getShortDescription(), changedRecipe.getShortDescription());
      assertEquals(newTestRecipe.getLink(), changedRecipe.getLink());
      assertEquals(newTestRecipe.getCookingTime(), changedRecipe.getCookingTime());
      assertEquals(newTestRecipe.getAmounts(), changedRecipe.getAmounts());
      assertEquals(newTestRecipe.getIngredients(), changedRecipe.getIngredients());
      assertEquals(newTestRecipe.getInstructions(), changedRecipe.getInstructions());
      assertEquals(newTestRecipe.getImage(), changedRecipe.getImage());
      assertEquals(newTestRecipe.getTags(), changedRecipe.getTags());
      assertEquals(newTestRecipe.getAuthorID(), oldRecipe.getAuthorID());
    }

    @Test
    public void deleteRecipe_validInputs_success() {

      User user = new User();
      user.setPassword("test");
      user.setUsername("test");
      user.setEmail("test");
      user.setCreationDate(new Date());
      user.setStatus(UserStatus.ONLINE);
      user.setToken("test");
      Cookbook cookbook = new Cookbook();
      cookbook.setStatus(CookbookStatus.PERSONAL);
      cookbook.setRecipes(new ArrayList<>());
      user.setCookbook(cookbook);
      cookbookRepository.save(cookbook);
      Calendar calendar = new Calendar();
      calendarRepository.save(calendar);
      user.setCalendar(calendar);

      userRepository.save(user);

      List<String> testlist = new ArrayList<>();
      testlist.add("test");

      List<RecipeTags> testtags = new ArrayList<>();
      testtags.add(RecipeTags.VEGAN);

      List<Long> testgroups = new ArrayList<>();

      Recipe testRecipe = new Recipe();
      testRecipe.setTitle("test");
      testRecipe.setShortDescription("test");
      testRecipe.setLink("test");
      testRecipe.setCookingTime("test");
      testRecipe.setAmounts(testlist);
      testRecipe.setIngredients(testlist);
      testRecipe.setInstructions(testlist);
      testRecipe.setImage("test");
      testRecipe.setTags(testtags);
      testRecipe.setGroups(testgroups);
      testRecipe.setAuthorID(user.getId());

      recipeRepository.save(testRecipe);

      // when
      recipeService.deleteRecipe(testRecipe);

      Recipe recipe = recipeRepository.findById(testRecipe.getId()).orElse(null);
      
      assertEquals(null, recipe);
    }

    @Test
    public void removeRecipeFromGroup_validInputs_success(){

      //create new recipe
      Recipe recipe = new Recipe();
      recipeRepository.save(recipe);

        //create cookbook and set status
      Cookbook cookbook = new Cookbook();
      cookbook.setStatus(CookbookStatus.GROUP);

        //add recipe to cookbook
      List<Long> recipes = new ArrayList<>();
      recipes.add(recipe.getId());
      cookbook.setRecipes(recipes);
      cookbookRepository.save(cookbook);

        //create group and set cookbook
      Group group = new Group();
      group.setName("name");
      group.setCookbook(cookbook);
      groupRepository.save(group);

        //add groupId to recipe
      List<Long> groups = new ArrayList<>();
      groups.add(group.getId());
      recipe.setGroups(groups);
      recipeRepository.save(recipe);

      groupRepository.flush();
      cookbookRepository.flush();
      recipeRepository.flush();

      //call method
      Group g = recipeService.removeRecipeFromGroup(group.getId(), recipe.getId());

      //groupCookbook does not contain recipes anymore
      assertEquals(new ArrayList<>(), g.getCookbook().getRecipes());

    }

    @Test
    public void removeRecipeFromGroup_GroupNotFound(){

        //create new recipe
        Recipe recipe = new Recipe();
        recipeRepository.save(recipe);

        //create cookbook and set status
        Cookbook cookbook = new Cookbook();
        cookbook.setStatus(CookbookStatus.GROUP);

        //add recipe to cookbook
        List<Long> recipes = new ArrayList<>();
        recipes.add(recipe.getId());
        cookbook.setRecipes(recipes);
        cookbookRepository.save(cookbook);

        //create group and set cookbook
        Group group = new Group();
        group.setName("name");
        group.setCookbook(cookbook);
        groupRepository.save(group);

        //add groupId to recipe
        List<Long> groups = new ArrayList<>();
        groups.add(group.getId());
        recipe.setGroups(groups);
        recipeRepository.save(recipe);

        groupRepository.flush();
        cookbookRepository.flush();
        recipeRepository.flush();

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(2L, recipe.getId()));

    }

    @Test
    public void removeRecipeFromGroup_RecipeNull(){

        //create new recipe
        Recipe recipe = new Recipe();
        recipeRepository.save(recipe);

        //create cookbook and set status
        Cookbook cookbook = new Cookbook();
        cookbook.setStatus(CookbookStatus.GROUP);

        cookbookRepository.save(cookbook);

        //create group and set cookbook
        Group group = new Group();
        group.setName("name");
        group.setCookbook(cookbook);
        groupRepository.save(group);

        //add groupId to recipe
        List<Long> groups = new ArrayList<>();
        groups.add(group.getId());
        recipe.setGroups(groups);
        recipeRepository.save(recipe);

        groupRepository.flush();
        cookbookRepository.flush();
        recipeRepository.flush();

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(group.getId(), recipe.getId()));

    }

    @Test
    public void removeRecipeFromGroup_RecipeNotInCookbook(){

        //create new recipe
        Recipe recipe = new Recipe();
        recipeRepository.save(recipe);

        //create cookbook and set status
        Cookbook cookbook = new Cookbook();
        cookbook.setStatus(CookbookStatus.GROUP);

        //add recipe to cookbook
        List<Long> recipes = new ArrayList<>();
        recipes.add(recipe.getId());
        cookbook.setRecipes(recipes);
        cookbookRepository.save(cookbook);

        //create group and set cookbook
        Group group = new Group();
        group.setName("name");
        group.setCookbook(cookbook);
        groupRepository.save(group);

        //add groupId to recipe
        List<Long> groups = new ArrayList<>();
        groups.add(group.getId());
        recipe.setGroups(groups);
        recipeRepository.save(recipe);

        groupRepository.flush();
        cookbookRepository.flush();
        recipeRepository.flush();

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(group.getId(), 8L));
    }

    @Test
    public void removeRecipeFromGroup_RecipeNotFound(){

        //create cookbook and set status
        Cookbook cookbook = new Cookbook();
        cookbook.setStatus(CookbookStatus.GROUP);
        cookbookRepository.save(cookbook);

        //create group and set cookbook
        Group group = new Group();
        group.setName("name");
        group.setCookbook(cookbook);
        groupRepository.save(group);

        groupRepository.flush();
        cookbookRepository.flush();

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(group.getId(), 1L));
    }


    @Test
    public void removeRecipeFromGroup_GroupNull(){

        //create new recipe
        Recipe recipe = new Recipe();
        recipeRepository.save(recipe);

        //create cookbook and set status
        Cookbook cookbook = new Cookbook();
        cookbook.setStatus(CookbookStatus.GROUP);

        //add recipe to cookbook
        List<Long> recipes = new ArrayList<>();
        recipes.add(recipe.getId());
        cookbook.setRecipes(recipes);
        cookbookRepository.save(cookbook);

        //create group and set cookbook
        Group group = new Group();
        group.setName("name");
        group.setCookbook(cookbook);
        groupRepository.save(group);

        groupRepository.flush();
        cookbookRepository.flush();
        recipeRepository.flush();

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(group.getId(), recipe.getId()));
    }

    @Test
    public void removeRecipeFromGroup_GroupNotInRecipe(){

        //create new recipe
        Recipe recipe = new Recipe();
        recipeRepository.save(recipe);

        //create cookbook and set status
        Cookbook cookbook = new Cookbook();
        cookbook.setStatus(CookbookStatus.GROUP);

        //add recipe to cookbook
        List<Long> recipes = new ArrayList<>();
        recipes.add(recipe.getId());
        cookbook.setRecipes(recipes);
        cookbookRepository.save(cookbook);

        //create group and set cookbook
        Group group = new Group();
        group.setName("name");
        group.setCookbook(cookbook);
        groupRepository.save(group);

        //add groupId to recipe
        List<Long> groups = new ArrayList<>();
        groups.add(group.getId());
        recipe.setGroups(groups);
        recipeRepository.save(recipe);

        groupRepository.flush();
        cookbookRepository.flush();
        recipeRepository.flush();

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(100L, recipe.getId()));
    }

    //  voteOnRecipe    //
    @Test
    public void voteOnRecipe_validInputFirstVote_success() {

        //create new recipe
        Recipe recipe = new Recipe();
        recipeRepository.save(recipe);

        //create votingDTO
        VotingDTO votingDTO = new VotingDTO();
        votingDTO.setVote(3.0);

        recipeService.voteOnRecipe(recipe.getId(), votingDTO);

        Optional<Recipe> votedRecipe = recipeRepository.findById(recipe.getId());

        assertEquals(votedRecipe.get().getVote(), 3.0);
        assertEquals(votedRecipe.get().getCount(), 1);
        assertEquals(votedRecipe.get().getSum(), 3);
    }

    @Test
    public void voteOnRecipe_validInputMultipleVotes_success() {

        //create new recipe
        Recipe recipe = new Recipe();
        recipe.setCount(1);
        recipe.setVote(4.0);
        recipe.setSum(4L);
        recipeRepository.save(recipe);

        //create votingDTO
        VotingDTO votingDTO = new VotingDTO();
        votingDTO.setVote(3.0);

        recipeService.voteOnRecipe(recipe.getId(), votingDTO);

        Optional<Recipe> votedRecipe = recipeRepository.findById(recipe.getId());

        assertEquals(votedRecipe.get().getVote(), 3.5);
        assertEquals(votedRecipe.get().getCount(), 2);
        assertEquals(votedRecipe.get().getSum(), 7);
    }

    @Test
    public void voteOnRecipe_RecipeNotFound_throwsException() {

        //create votingDTO
        VotingDTO votingDTO = new VotingDTO();
        votingDTO.setVote(3.0);

        assertThrows(ResponseStatusException.class, () -> recipeService.voteOnRecipe(1L, votingDTO));
    }


    //  deleteComment   //
    @Test
    public void deleteComment_validInput_success() {

        Comment testComment1 = new Comment();
        testComment1 = new Comment();
        testComment1.setId(1L);
        testComment1.setUsername("username");
        testComment1.setText("text");
        testComment1.setUserID(3L);

        Comment testComment2 = new Comment();
        testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment1.getId());
        comments.add(testComment2.getId());

        //create new recipe
        Recipe recipe = new Recipe();
        recipe.setComments(comments);
        recipeRepository.save(recipe);

        recipeService.deleteComment(recipe, testComment1);
    }

    @Test
    public void deleteComment_CommentNotInRecipe_throwsException() {

        Comment testComment1 = new Comment();
        testComment1.setId(1L);
        testComment1.setUsername("username");
        testComment1.setText("text");
        testComment1.setUserID(3L);

        Comment testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        commentRepository.save(testComment1);
        commentRepository.save(testComment2);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment1.getId());

        //create new recipe
        Recipe recipe = new Recipe();
        recipe.setComments(comments);
        recipeRepository.save(recipe);

        assertThrows(ResponseStatusException.class, () -> recipeService.deleteComment(recipe, testComment2));
    }

    //  addComment      //
    @Test
    public void addComment_validInput_success() {

        //create comment to add to recipe
        Comment testComment1 = new Comment();
        testComment1.setId(1L);
        testComment1.setUsername("username");
        testComment1.setText("text");
        testComment1.setUserID(3L);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment1.getId());

        //create new recipe
        Recipe recipe = new Recipe();
        recipe.setComments(comments);
        recipeRepository.save(recipe);

        //new comment to add to recipe
        Comment testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        recipeService.addComment(recipe.getId(), testComment2);
    }

    @Test
    public void addComment_RecipeNotFound_throwsException() {

        //new comment to add to recipe
        Comment testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        assertThrows(ResponseStatusException.class, () ->  recipeService.addComment(2L, testComment2));
    }

    @Test
    public void addComment_CommentAlreadyPartOf_throwsException() {

        //create comment to add to recipe
        Comment testComment1 = new Comment();
        testComment1.setId(1L);
        testComment1.setUsername("username");
        testComment1.setText("text");
        testComment1.setUserID(3L);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment1.getId());

        //create new recipe
        Recipe recipe = new Recipe();
        recipe.setComments(comments);
        recipeRepository.save(recipe);

        assertThrows(ResponseStatusException.class, () ->  recipeService.addComment(2L, testComment1));
    }

}
