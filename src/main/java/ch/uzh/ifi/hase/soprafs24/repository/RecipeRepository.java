package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository("recipeRepository")
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
  
  Recipe findById(long recipeID);
  
}
