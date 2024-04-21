package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RecipeRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private RecipeRepository recipeRepository;

  @Test
  public void findById_success() {
    // given
    List<String> testlist = Arrays.asList("T", "E", "S", "T");
    List<RecipeTags> testtags = Arrays.asList(RecipeTags.VEGAN);
    List<Long> testgroups = Arrays.asList(10L);

    Recipe recipe = new Recipe();
    recipe.setTitle("test");
    recipe.setShortDescription("test");
    recipe.setLink("test");
    recipe.setCookingTime("test");
    recipe.setAmounts(testlist);
    recipe.setIngredients(testlist);
    recipe.setInstructions(testlist);
    recipe.setImage("test");
    recipe.setTags(testtags);
    recipe.setGroups(testgroups);
    recipe.setAuthorID(5L);

    entityManager.persist(recipe);
    entityManager.flush();

    // when
    Optional<Recipe> found = recipeRepository.findById(recipe.getId());

    // then
    assertNotNull(found.get());
  }

  @Test
  public void findByAuthorID_success(){
    // given
    List<String> testlist = Arrays.asList("T", "E", "S", "T");
    List<RecipeTags> testtags = Arrays.asList(RecipeTags.VEGAN);
    List<Long> testgroups = Arrays.asList(10L);

    Recipe recipe = new Recipe();
    recipe.setTitle("test");
    recipe.setShortDescription("test");
    recipe.setLink("test");
    recipe.setCookingTime("test");
    recipe.setAmounts(testlist);
    recipe.setIngredients(testlist);
    recipe.setInstructions(testlist);
    recipe.setImage("test");
    recipe.setTags(testtags);
    recipe.setGroups(testgroups);
    recipe.setAuthorID(5L);

    Recipe recipe2 = new Recipe();
    recipe2.setTitle("test");
    recipe2.setShortDescription("test");
    recipe2.setLink("test");
    recipe2.setCookingTime("test");
    recipe2.setAmounts(testlist);
    recipe2.setIngredients(testlist);
    recipe2.setInstructions(testlist);
    recipe2.setImage("test");
    recipe2.setTags(testtags);
    recipe2.setGroups(testgroups);
    recipe2.setAuthorID(5L);

    entityManager.persist(recipe);
    entityManager.persist(recipe2);
    entityManager.flush();

    // when
    List<Recipe> found = recipeRepository.findByAuthorID(5L);

    assertNotNull(found);
  }

}
