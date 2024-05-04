package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(@Qualifier("commentRepository") CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    public Comment createComment (Comment newComment) {

        newComment = commentRepository.save(newComment);
        commentRepository.flush();

        log.debug("created information fro User: {}", newComment);
        return newComment;
    }

}

