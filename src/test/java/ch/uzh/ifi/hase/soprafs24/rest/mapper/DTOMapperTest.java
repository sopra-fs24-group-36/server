package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.CalendarRequest;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ItemRequest;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
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
        user.setInvitations(new ArrayList<>());

        // MAP -> Create user
        UserDTO userDTO = DTOMapper.INSTANCE.convertEntityToUserDTO(user);

        // check content
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertNotNull(user.getCreationDate());
        assertEquals(user.getStatus(), userDTO.getStatus());
        assertEquals(user.getInvitations(), userDTO.getInvitations());
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
    
        List<Long> groupIDs = new ArrayList<>();
        groupIDs.add(1L);
        groupIDs.add(2L);
        recipePostDTO.setGroups(groupIDs);

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
        assertEquals(recipe.getGroups(), recipePostDTO.getGroups());
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

        List<Long> groupIDs = new ArrayList<>();
        groupIDs.add(1L);
        groupIDs.add(2L);
        recipe.setGroups(groupIDs);

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
        assertEquals(recipe.getGroups(), recipeDTO.getGroups());
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

        List<Long> groupIDs = new ArrayList<>();
        groupIDs.add(1L);
        groupIDs.add(2L);
        recipePutDTO.setGroups(groupIDs);

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
        assertEquals(recipe.getGroups(), recipePutDTO.getGroups());
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


    //test the UserPutDTOtoUser
    @Test
    public void testUpdateUser_fromUserPutDTO_toUser_success() {
        // create UserPostDTO
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setName("name");
        userPutDTO.setUsername("username");
        userPutDTO.setEmail("email.email@email.com");
        userPutDTO.setProfilePicture("pic");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // check content
        assertEquals(userPutDTO.getName(), user.getName());
        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getEmail(), user.getEmail());
        assertEquals(userPutDTO.getProfilePicture(), user.getProfilePicture());
    }

    @Test
    public void testCreateShoppingList_fromShoppingList_toShoppingListDTO_success(){
        ShoppingList shoppingList = new ShoppingList();
        List<String> items = new ArrayList<>();
        items.add("Apple");
        shoppingList.setItems(items);

        // MAP
        ShoppingListDTO shoppingListDTO = DTOMapper.INSTANCE.convertEntityToShoppingListDTO(shoppingList);

        assertEquals(shoppingList.getItems(), shoppingListDTO.getItems());
    }

    @Test
    public void testConvertItemPost_fromItemPostDTO_toItemRequest_success(){
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        itemPostDTO.setItem("Apple");

        ItemRequest itemRequest = DTOMapper.INSTANCE.convertItemPostDTOtoEntity(itemPostDTO);

        assertEquals(itemPostDTO.getItem(), itemRequest.getItem());
    }

    @Test
    public void testConvertItemPut_fromItemPutDTO_toItemRequest_success(){
        ItemPutDTO itemPutDTO = new ItemPutDTO();
        itemPutDTO.setItem("Apple");

        ItemRequest itemRequest = DTOMapper.INSTANCE.convertItemPutDTOtoEntity(itemPutDTO);

        assertEquals(itemPutDTO.getItem(), itemRequest.getItem());
    }

    @Test
    public void testConvertDateRecipePostDTO_toCalendarRequest_success(){
        DateRecipeDTO dateRecipeDTO = new DateRecipeDTO();
        dateRecipeDTO.setDate(new Date());
        dateRecipeDTO.setRecipeID(1L);
        dateRecipeDTO.setStatus(CalendarStatus.BREAKFAST);

        // MAP
        CalendarRequest calendarRequest = DTOMapper.INSTANCE.convertDateRecipePostDTOtoEntity(dateRecipeDTO);

        assertEquals(dateRecipeDTO.getDate(), calendarRequest.getDate());
        assertEquals(dateRecipeDTO.getRecipeID(), calendarRequest.getRecipeID());
        assertEquals(dateRecipeDTO.getStatus(), calendarRequest.getStatus());
    }


}