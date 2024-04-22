package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
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

    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        groupRepository.deleteAll();
        userRepository.deleteAll();
        recipeRepository.deleteAll();
        cookbookRepository.deleteAll();
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

    @Test
    public void updateRecipe_validInputs_success() {

      List<String> testlist = new ArrayList<>();
      testlist.add("test");

      List<RecipeTags> testtags = new ArrayList<>();
      testtags.add(RecipeTags.VEGAN);

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
      testRecipe.setAuthorID(1L);
      recipeRepository.save(testRecipe);

      List<String> testlistupdate = new ArrayList<>();
      testlistupdate.add("testupdate");

      List<RecipeTags> testtagsupdate = new ArrayList<>();
      testtagsupdate.add(RecipeTags.VEGETARIAN);

      Recipe updatedRecipe = new Recipe();
      updatedRecipe.setTitle("testupdate");
      updatedRecipe.setShortDescription("testupdate");
      updatedRecipe.setLink("testupdate");
      updatedRecipe.setCookingTime("testupdate");
      updatedRecipe.setAmounts(testlistupdate);
      updatedRecipe.setIngredients(testlistupdate);
      updatedRecipe.setInstructions(testlistupdate);
      updatedRecipe.setImage("testupdate");
      updatedRecipe.setTags(testtagsupdate);

      // update the recipe
      recipeService.updateRecipe(testRecipe.getId(), updatedRecipe);

      // check that the updated stuff is not the same since we changed everything
      assertNotEquals(testRecipe.getTitle(), updatedRecipe.getTitle());
      assertNotEquals(testRecipe.getShortDescription(), updatedRecipe.getShortDescription());
      assertNotEquals(testRecipe.getLink(), updatedRecipe.getLink());
      assertNotEquals(testRecipe.getCookingTime(), updatedRecipe.getCookingTime());
      assertNotEquals(testRecipe.getAmounts(), updatedRecipe.getAmounts());
      assertNotEquals(testRecipe.getIngredients(), updatedRecipe.getIngredients());
      assertNotEquals(testRecipe.getInstructions(), updatedRecipe.getInstructions());
      assertNotEquals(testRecipe.getImage(), updatedRecipe.getImage());
      assertNotEquals(testRecipe.getTags(), updatedRecipe.getTags());
    }

    @Test
    public void deleteRecipe_validInputs_success() {

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
      recipeService.deleteRecipe(testRecipe);

      Recipe recipe = recipeRepository.findById(testRecipe.getId()).orElse(null);
      
      assertEquals(null, recipe);
    }

    @Test
    public void removeRecipeFromGroup_validInputs_success(){

      Cookbook cookbook = new Cookbook();
      cookbook.setStatus(CookbookStatus.GROUP);
      List<Long> recipes = new ArrayList<>();
      recipes.add(20L);
      cookbook.setRecipes(recipes);
      cookbookRepository.save(cookbook);
      cookbookRepository.flush();

      Group group = new Group();
      group.setName("test");
      group.setCookbook(cookbook);

      groupRepository.save(group);
      groupRepository.flush();

      Group g = recipeService.removeRecipeFromGroup(group.getId(), 20L);
      
      assertEquals(new ArrayList<>(), g.getCookbook().getRecipes());;
    }
}
