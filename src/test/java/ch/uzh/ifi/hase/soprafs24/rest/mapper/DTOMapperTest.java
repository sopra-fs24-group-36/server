package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */


public class DTOMapperTest {

    //test the UserPostDTOtoUser with the createUser method
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("name");
        userPostDTO.setUsername("username");
        userPostDTO.setEmail("email.email@email.com");
        userPostDTO.setPassword("password");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getName(), user.getName());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getEmail(), user.getEmail());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
    }

    //test the UserPostDTOtoUser with the createUser method
    @Test
    public void testCreateUser_fromUser_toUserDTO_success() {
        // create UserPostDTO
        User user = new User();
        user.setName("name");
        user.setUsername("username");
        user.setEmail("email.email@email.com");
        user.setPassword("password");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");
        Date creationDate = new Date();
        user.setCreationDate(creationDate);

        // MAP -> Create user
        UserDTO userDTO = DTOMapper.INSTANCE.convertEntityToUserDTO(user);

        // check content
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertNotNull(user.getCreationDate());
        assertEquals(user.getStatus(), userDTO.getStatus());
    }

    @Test
    public void testCreateRecipe_fromRecipePostDTO_toRecipe_success(){
        RecipePostDTO recipePostDTO = new RecipePostDTO();
        recipePostDTO.setTitle("testrecipename");
        recipePostDTO.setShortDescription("testdescription");
        recipePostDTO.setLink("testlink");
        recipePostDTO.setCookingTime("testtime");
    
        List<String> amounts = new ArrayList<>();
        amounts.add("testamount 1");
        amounts.add("testamount 2");
        recipePostDTO.setAmounts(amounts);
    
        List<String> ingredients = new ArrayList<>();
        ingredients.add("testingredient 1");
        ingredients.add("testingredient 2");
        recipePostDTO.setIngredients(ingredients);
    
        List<String> instructions = new ArrayList<>();
        instructions.add("testinstruction 1");
        instructions.add("testinstruction 2");
        recipePostDTO.setInstructions(instructions);
    
        recipePostDTO.setImage("testimage");
    
        List<RecipeTags> tags = new ArrayList<>();
        tags.add(RecipeTags.VEGAN);
        tags.add(RecipeTags.VEGETARIAN);
        recipePostDTO.setTags(tags);
    
        List<Long> cookbooks = new ArrayList<>();
        cookbooks.add(1L);
        cookbooks.add(2L);
        recipePostDTO.setCookbooks(cookbooks);

        // MAP -> Create recipe
        Recipe recipe = DTOMapper.INSTANCE.convertRecipePostDTOtoEntity(recipePostDTO);

        // check content
        assertEquals(recipe.getTitle(), recipePostDTO.getTitle());
        assertEquals(recipe.getShortDescription(), recipePostDTO.getShortDescription());
        assertEquals(recipe.getLink(), recipePostDTO.getLink());
        assertEquals(recipe.getCookingTime(), recipePostDTO.getCookingTime());
        assertEquals(recipe.getAmounts(), recipePostDTO.getAmounts());
        assertEquals(recipe.getIngredients(), recipePostDTO.getIngredients());
        assertEquals(recipe.getInstructions(), recipePostDTO.getInstructions());
        assertEquals(recipe.getImage(), recipePostDTO.getImage());
        assertEquals(recipe.getTags().size(), recipePostDTO.getTags().size());
        for (int i = 0; i < recipe.getTags().size(); i++) {
            assertEquals(recipe.getTags().get(i), recipePostDTO.getTags().get(i));
        }
        assertEquals(recipe.getCookbooks(), recipePostDTO.getCookbooks());
    }

    @Test
    public void testCreateRecipe_fromRecipe_toRecipeDTO_success(){
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setTitle("testrecipename");
        recipe.setShortDescription("testdescription");
        recipe.setLink("testlink");
        recipe.setCookingTime("testtime");

        List<String> amounts = new ArrayList<>();
        amounts.add("testamount 1");
        amounts.add("testamount 2");
        recipe.setAmounts(amounts);

        List<String> ingredients = new ArrayList<>();
        ingredients.add("testingredient 1");
        ingredients.add("testingredient 2");
        recipe.setIngredients(ingredients);

        List<String> instructions = new ArrayList<>();
        instructions.add("testinstruction 1");
        instructions.add("testinstruction 2");
        recipe.setInstructions(instructions);

        recipe.setImage("testimage");

        List<RecipeTags> tags = new ArrayList<>();
        tags.add(RecipeTags.VEGAN);
        tags.add(RecipeTags.VEGETARIAN);
        recipe.setTags(tags);

        List<Long> cookbooks = new ArrayList<>();
        cookbooks.add(1L);
        cookbooks.add(2L);
        recipe.setCookbooks(cookbooks);

        recipe.setAuthorID(1L);

        // MAP -> Create recipe
        RecipeDTO recipeDTO = DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe);

        // Check content
        assertEquals(recipe.getId(), recipeDTO.getId());
        assertEquals(recipe.getTitle(), recipeDTO.getTitle());
        assertEquals(recipe.getShortDescription(), recipeDTO.getShortDescription());
        assertEquals(recipe.getLink(), recipeDTO.getLink());
        assertEquals(recipe.getCookingTime(), recipeDTO.getCookingTime());
        assertEquals(recipe.getAmounts(), recipeDTO.getAmounts());
        assertEquals(recipe.getIngredients(), recipeDTO.getIngredients());
        assertEquals(recipe.getInstructions(), recipeDTO.getInstructions());
        assertEquals(recipe.getImage(), recipeDTO.getImage());
        assertEquals(recipe.getTags().size(), recipeDTO.getTags().size());
        for (int i = 0; i < recipe.getTags().size(); i++) {
            assertEquals(recipe.getTags().get(i), recipeDTO.getTags().get(i));
        }
        assertEquals(recipe.getCookbooks(), recipeDTO.getCookbooks());
    }

    @Test
    public void testCreateRecipe_fromRecipePutDTO_toRecipe_success(){
        RecipePutDTO recipePutDTO = new RecipePutDTO();
        recipePutDTO.setTitle("testrecipename");
        recipePutDTO.setShortDescription("testdescription");
        recipePutDTO.setLink("testlink");
        recipePutDTO.setCookingTime("testtime");

        List<String> amounts = new ArrayList<>();
        amounts.add("testamount 1");
        amounts.add("testamount 2");
        recipePutDTO.setAmounts(amounts);

        List<String> ingredients = new ArrayList<>();
        ingredients.add("testingredient 1");
        ingredients.add("testingredient 2");
        recipePutDTO.setIngredients(ingredients);

        List<String> instructions = new ArrayList<>();
        instructions.add("testinstruction 1");
        instructions.add("testinstruction 2");
        recipePutDTO.setInstructions(instructions);

        recipePutDTO.setImage("testimage");

        List<RecipeTags> tags = new ArrayList<>();
        tags.add(RecipeTags.VEGAN);
        tags.add(RecipeTags.VEGETARIAN);
        recipePutDTO.setTags(tags);

        List<Long> cookbooks = new ArrayList<>();
        cookbooks.add(1L);
        cookbooks.add(2L);
        recipePutDTO.setCookbooks(cookbooks);

        // MAP -> Create recipe
        Recipe recipe = DTOMapper.INSTANCE.convertRecipePutDTOtoEntity(recipePutDTO);

        // Check content
        assertEquals(recipe.getTitle(), recipePutDTO.getTitle());
        assertEquals(recipe.getShortDescription(), recipePutDTO.getShortDescription());
        assertEquals(recipe.getLink(), recipePutDTO.getLink());
        assertEquals(recipe.getCookingTime(), recipePutDTO.getCookingTime());
        assertEquals(recipe.getAmounts(), recipePutDTO.getAmounts());
        assertEquals(recipe.getIngredients(), recipePutDTO.getIngredients());
        assertEquals(recipe.getInstructions(), recipePutDTO.getInstructions());
        assertEquals(recipe.getImage(), recipePutDTO.getImage());
        assertEquals(recipe.getTags().size(), recipePutDTO.getTags().size());
        for (int i = 0; i < recipe.getTags().size(); i++) {
            assertEquals(recipe.getTags().get(i), recipePutDTO.getTags().get(i));
        }
        assertEquals(recipe.getCookbooks(), recipePutDTO.getCookbooks());
    }


    @Test
    public void testCreateGroup_fromGroupPostDTO_toGroup_success(){
        GroupPostDTO groupPostDTO = new GroupPostDTO();

        groupPostDTO.setName("testgroupname");

        List<Long> members = new ArrayList<>();
        members.add(1L);
        members.add(2L);
        groupPostDTO.setMembers(members);

        Group group = DTOMapper.INSTANCE.convertGroupPostDTOtoEntity(groupPostDTO);

        assertEquals(group.getName(), groupPostDTO.getName());
        for (int i = 0; i < group.getMembers().size(); i++) {
            assertEquals(group.getMembers().get(i), groupPostDTO.getMembers().get(i));
        }
    }

    @Test
    public void testCreateGroup_fromGroup_toGroupDTO_success(){
        Group group = new Group();

        group.setName("testgroupname");

        List<Long> members = new ArrayList<>();
        members.add(1L);
        members.add(2L);
        group.setMembers(members);

        group.setId(1L);

        GroupDTO groupDTO = DTOMapper.INSTANCE.convertEntityToGroupDTO(group);

        assertEquals(group.getId(), groupDTO.getId());
        for (int i = 0; i < group.getMembers().size(); i++) {
            assertEquals(group.getMembers().get(i), groupDTO.getMembers().get(i));
        }
        assertEquals(group.getName(), groupDTO.getName());
    }




}