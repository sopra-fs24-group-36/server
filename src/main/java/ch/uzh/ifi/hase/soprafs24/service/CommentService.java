package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

// Service to handle all comment related functionality
@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    @Autowired
    public CommentService(@Qualifier("commentRepository") CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    // Create a new comment
    public Comment createComment (Comment newComment) {

        Long userID = newComment.getUserID();

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String username = user.getUsername();
        newComment.setUsername(username);

        newComment = commentRepository.save(newComment);
        commentRepository.flush();

        log.debug("created information fro User: {}", newComment);
        return newComment;
    }

    // Update a comment
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

    // Delete a comment
    public void deleteComment(Comment comment) {

        commentRepository.delete(comment);
        commentRepository.flush();
    }
}

