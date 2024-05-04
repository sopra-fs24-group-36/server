package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;


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

    public void updateComment(Long commentID, Comment commentUpdate) {

        //check if comment with this ID exists
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        //update comment
        String newText = commentUpdate.getText().trim();

        if (!newText.isEmpty()) {
            comment.setText(commentUpdate.getText());
            commentRepository.save(comment);
            commentRepository.flush();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Need to have at least length 1");
        }


    }

    public void deleteComment(Comment comment) {

        commentRepository.delete(comment);
        commentRepository.flush();
    }
}

