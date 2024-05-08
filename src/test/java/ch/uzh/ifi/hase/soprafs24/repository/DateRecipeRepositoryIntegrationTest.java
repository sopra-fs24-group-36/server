package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.DateRecipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class DateRecipeRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private DateRecipeRepository dateRecipeRepository;

  @Test
  public void findById_success() {

    Calendar calendar = new Calendar();
    entityManager.persist(calendar);
    entityManager.flush();

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(new Date());
    dateRecipe.setRecipeID(10L);
    dateRecipe.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe.setCalendar(calendar);

    entityManager.persist(dateRecipe);
    entityManager.flush();

    // when
    Optional<DateRecipe> found = dateRecipeRepository.findById(dateRecipe.getId());

    // then
    assertNotNull(found.get());
  }
}