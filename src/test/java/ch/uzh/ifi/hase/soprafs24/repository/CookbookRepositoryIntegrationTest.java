package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CookbookRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CookbookRepository cookbookRepository;

  @Test
  public void findById_success() {
    // given
    List<Long> testrecipes = Arrays.asList(10L, 20L);

    Cookbook cookbook = new Cookbook();
    cookbook.setStatus(CookbookStatus.PERSONAL);
    cookbook.setRecipes(testrecipes);

    entityManager.persist(cookbook);
    entityManager.flush();

    // when
    Optional<Cookbook> found = cookbookRepository.findById(cookbook.getId());

    // then
    assertNotNull(found.get());
  }
}
