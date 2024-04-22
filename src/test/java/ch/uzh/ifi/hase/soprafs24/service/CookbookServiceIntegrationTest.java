package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
public class CookbookServiceIntegrationTest {

    @Qualifier("cookbookRepository")
    @Autowired
    private CookbookRepository cookbookRepository;

    @Autowired
    private CookbookService cookbookService;

    @BeforeEach
    public void setup() {
        cookbookRepository.deleteAll();
    }

    //  test createGroup method //
    @Test
    public void createGroup_validInputs_success() {

        Cookbook testCookbook = new Cookbook();
        testCookbook.setStatus(CookbookStatus.PERSONAL);
        List<Long> recipes = new ArrayList<>();
        testCookbook.setRecipes(recipes);

        // when
        Cookbook createdCookbook = cookbookService.createCookbook(testCookbook);

        // then
        assertEquals(testCookbook.getId(), createdCookbook.getId());
        assertEquals(testCookbook.getStatus(), createdCookbook.getStatus());
        assertEquals(testCookbook.getRecipes(), createdCookbook.getRecipes());

    }
}
