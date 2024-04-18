package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ShoppingListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

  public void addItemGroup(String item, Group group){
    ShoppingList shoppingList = group.getShoppingList();

    List<String> items = shoppingList.getItems();
    if(!items.contains(item)){
      items.add(item);
      shoppingListRepository.save(shoppingList);
      shoppingListRepository.flush();
    }
  }

  public void addItemUser(String item, User user){
    ShoppingList shoppingList = user.getShoppingList();

    List<String> items = shoppingList.getItems();
    if(!items.contains(item)){
      items.add(item);
      shoppingListRepository.save(shoppingList);
      shoppingListRepository.flush();
    }
  }

  public void removeGroupItem(String item, Group group){
    ShoppingList shoppingList = group.getShoppingList();

    List<String> items = shoppingList.getItems();
    if(items.contains(item)){
      items.remove(item);
      shoppingListRepository.save(shoppingList);
      shoppingListRepository.flush();
    }
  }

  public void removeUserItem(String item, User user){
    ShoppingList shoppingList = user.getShoppingList();

    List<String> items = shoppingList.getItems();
    if(items.contains(item)){
      items.remove(item);
      shoppingListRepository.save(shoppingList);
      shoppingListRepository.flush();
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "item is not on your shopping list, nothing to tick off!");
    }
  }
}