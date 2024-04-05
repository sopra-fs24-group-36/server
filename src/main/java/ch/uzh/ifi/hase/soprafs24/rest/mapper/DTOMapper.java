package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePutDTO;
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
    @Mapping(source = "ingredients" , target = "ingredients")
    @Mapping(source = "instructions" , target = "instructions")
    @Mapping(source = "image" , target = "image")
    @Mapping(source = "tags" , target = "tags")
    @Mapping(source = "cookbooks" , target = "cookbooks")
    Recipe convertUserPostDTOtoEntity(RecipePostDTO recipePostDTO);

    @Mapping(source = "id" , target = "id")
    Recipe convertUserGetDTOtoEntity(RecipeGetDTO recipeGetDTO);

    @Mapping(source = "id" , target = "id")
    Recipe convertUserPutDTOtoEntity(RecipePutDTO recipePutDTO);

    @Mapping(source = "id" , target = "id")
    Recipe convertUserDeleteDTOtoEntity(RecipeDeleteDTO recipeDeleteDTO);

    @Mapping(source = "id" , target = "id")
    @Mapping(source = "title" , target = "title")
    @Mapping(source = "shortDescription" , target = "shortDescription")
    @Mapping(source = "link" , target = "link")
    @Mapping(source = "cookingTime" , target = "cookingTime")
    @Mapping(source = "ingredients" , target = "ingredients")
    @Mapping(source = "instructions" , target = "instructions")
    @Mapping(source = "image" , target = "image")
    @Mapping(source = "tags" , target = "tags")
    @Mapping(source = "cookbooks" , target = "cookbooks")
    RecipeDTO convertEntityToRecipeDTO(Recipe recipe);

}
