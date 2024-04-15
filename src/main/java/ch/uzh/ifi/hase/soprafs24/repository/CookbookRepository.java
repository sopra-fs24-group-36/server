package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository("cookbookRepository")
public interface CookbookRepository extends JpaRepository<Cookbook, Long> {

  Cookbook findById(long id);
  
}
