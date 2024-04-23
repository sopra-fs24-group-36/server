package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookbookServiceTest {

  @Mock
  private CookbookRepository cookbookRepository;

  @InjectMocks
  private CookbookService cookbookService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void createCookbook_ValidInput_CookbookCreated() {
    // Given
    Cookbook newCookbook = new Cookbook();
    when(cookbookRepository.save(any())).thenReturn(newCookbook);

    // When
    Cookbook createdCookbook = cookbookService.createCookbook(newCookbook);

    // Then
    assertNotNull(createdCookbook);
    assertEquals(newCookbook, createdCookbook);
    verify(cookbookRepository, times(1)).save(any());
  }
}
