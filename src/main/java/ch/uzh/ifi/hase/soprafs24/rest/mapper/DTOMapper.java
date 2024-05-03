package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.CalendarRequest;
import ch.uzh.ifi.hase.soprafs24.entity.DateRecipe;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ItemRequest;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "profilePicture", target = "profilePicture")
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
    @Mapping(source = "groups" , target = "groups")
    Recipe convertRecipePostDTOtoEntity(RecipePostDTO recipePostDTO);

    @Mapping(source = "title" , target = "title")
    @Mapping(source = "shortDescription" , target = "shortDescription")
    @Mapping(source = "link" , target = "link")
    @Mapping(source = "cookingTime" , target = "cookingTime")
    @Mapping(source = "amounts" , target = "amounts")
    @Mapping(source = "ingredients" , target = "ingredients")
    @Mapping(source = "instructions" , target = "instructions")
    @Mapping(source = "image" , target = "image")
    @Mapping(source = "tags" , target = "tags")
    @Mapping(source = "groups" , target = "groups")
    Recipe convertRecipePutDTOtoEntity(RecipePutDTO recipePutDTO);

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
    @Mapping(source = "groups" , target = "groups")
    @Mapping(source = "authorID" , target = "authorID")
    RecipeDTO convertEntityToRecipeDTO(Recipe recipe);

    @Mapping(source = "name" , target = "name")
    @Mapping(source = "membersNames" , target = "membersNames")
    @Mapping(source = "image" , target = "image")
    Group convertGroupPostDTOtoEntity(GroupPostDTO groupPostDTO);

    @Mapping(source = "id" , target = "id")
    @Mapping(source = "name" , target = "name")
    @Mapping(source = "members" , target = "members")
    @Mapping(source = "image" , target = "image")
    GroupDTO convertEntityToGroupDTO(Group group);

    @Mapping(source = "items" , target = "items")
    ShoppingListDTO convertEntityToShoppingListDTO(ShoppingList shoppingList);

    @Mapping(source = "item", target = "item")
    ItemRequest convertItemPostDTOtoEntity(ItemPostDTO itemPostDTO);

    @Mapping(source = "item", target = "item")
    ItemRequest convertItemPutDTOtoEntity(ItemPutDTO itemPostDTO);

    @Mapping(source = "username", target="username")
    @Mapping(source = "name", target = "name")
    @Mapping (source = "email", target = "email")
    @Mapping(source = "profilePicture", target = "profilePicture")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "date", target = "date")
    @Mapping(source = "recipeID", target = "recipeID")
    CalendarRequest convertDateRecipePostDTOtoEntity(DateRecipeDTO dateRecipePostDTO);
}
