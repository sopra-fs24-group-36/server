package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.DateRecipe;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;


@Repository("dateRecipeRepository")
public interface DateRecipeRepository extends JpaRepository<DateRecipe, Long> {

  List<DateRecipe> findByDateAndRecipeIDAndCalendarAndStatus(Date date, Long recipeID, Calendar calendar, CalendarStatus status);

  Optional<DateRecipe> findById(Long id);
}