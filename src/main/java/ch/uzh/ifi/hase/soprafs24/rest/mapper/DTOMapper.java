package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ShoppingListDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ShoppingListGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "creationDate", target = "creationDate", dateFormat = "YYYY-MM-DD")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "profilePicture", target = "profilePicture")
    UserDTO convertEntityToUserDTO(User user);

    @Mapping(source = "title" , target = "title")
    @Mapping(source = "shortDescription" , target = "shortDescription")
    @Mapping(source = "link" , target = "link")
    @Mapping(source = "cookingTime" , target = "cookingTime")
    @Mapping(source = "amounts" , target = "amounts")
    @Mapping(source = "ingredients" , target = "ingredients")
    @Mapping(source = "instructions" , target = "instructions")
    @Mapping(source = "image" , target = "image")
    @Mapping(source = "tags" , target = "tags")
    @Mapping(source = "cookbooks" , target = "cookbooks")
    Recipe convertRecipePostDTOtoEntity(RecipePostDTO recipePostDTO);

    @Mapping(source = "id" , target = "id")
    Recipe convertRecipeGetDTOtoEntity(RecipeGetDTO recipeGetDTO);

    @Mapping(source = "title" , target = "title")
    @Mapping(source = "shortDescription" , target = "shortDescription")
    @Mapping(source = "link" , target = "link")
    @Mapping(source = "cookingTime" , target = "cookingTime")
    @Mapping(source = "amounts" , target = "amounts")
    @Mapping(source = "ingredients" , target = "ingredients")
    @Mapping(source = "instructions" , target = "instructions")
    @Mapping(source = "image" , target = "image")
    @Mapping(source = "tags" , target = "tags")
    @Mapping(source = "cookbooks" , target = "cookbooks")
    Recipe convertRecipePutDTOtoEntity(RecipePutDTO recipePutDTO);

    @Mapping(source = "id" , target = "id")
    Recipe convertRecipeDeleteDTOtoEntity(RecipeDeleteDTO recipeDeleteDTO);

    @Mapping(source = "id" , target = "id")
    @Mapping(source = "title" , target = "title")
    @Mapping(source = "shortDescription" , target = "shortDescription")
    @Mapping(source = "link" , target = "link")
    @Mapping(source = "cookingTime" , target = "cookingTime")
    @Mapping(source = "amounts" , target = "amounts")
    @Mapping(source = "ingredients" , target = "ingredients")
    @Mapping(source = "instructions" , target = "instructions")
    @Mapping(source = "image" , target = "image")
    @Mapping(source = "tags" , target = "tags")
    @Mapping(source = "cookbooks" , target = "cookbooks")
    @Mapping(source = "authorID" , target = "authorID")
    RecipeDTO convertEntityToRecipeDTO(Recipe recipe);

    @Mapping(source = "name" , target = "name")
    @Mapping(source = "members" , target = "members")
    Group convertGroupPostDTOtoEntity(GroupPostDTO groupPostDTO);

    @Mapping(source = "id" , target = "id")
    @Mapping(source = "name" , target = "name")
    @Mapping(source = "members" , target = "members")
    GroupDTO convertEntityToGroupDTO(Group group);

    @Mapping(source = "id" , target = "id")
    ShoppingList convertShoppingListGetDTOtoEntity(ShoppingListGetDTO shoppingListGetDTO);
    
    @Mapping(source = "id" , target = "id")
    ShoppingList convertShoppingListDeleteDTOtoEntity(ShoppingListDeleteDTO shoppingListDeleteDTO);

    @Mapping(source = "id" , target = "id")
    @Mapping(source = "items" , target = "items")
    ShoppingList convertEntityToShoppingListDTO(ShoppingList shoppingList);

    @Mapping(source = "id", target = "id")
    User convertUserGetDTOtoEntity(UserGetDTO userGetDTO);
}
