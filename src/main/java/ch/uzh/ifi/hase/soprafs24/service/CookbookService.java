package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CookbookService {

    private final Logger log = LoggerFactory.getLogger(CookbookService.class);

    private final CookbookRepository cookbookRepository;

    @Autowired
    public CookbookService(@Qualifier("cookbookRepository") CookbookRepository cookbookRepository) {
        this.cookbookRepository = cookbookRepository;
    }


}
