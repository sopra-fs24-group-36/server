package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.repository.ShoppingListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class ShoppingListService {

  private final Logger log = LoggerFactory.getLogger(ShoppingListService.class);

  private final ShoppingListRepository shoppingListRepository;

  @Autowired
  public ShoppingListService(@Qualifier("shoppingListRepository") ShoppingListRepository shoppingListRepository) {
    this.shoppingListRepository = shoppingListRepository;
  }


  public ShoppingList createShoppingList(ShoppingList newShoppingList) {

    newShoppingList = shoppingListRepository.save(newShoppingList);
    shoppingListRepository.flush();

    log.debug("Created new Shopping List: {}", newShoppingList);

    return newShoppingList;
  }

}