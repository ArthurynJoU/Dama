package org.dama.service.impl.jpa;

import org.dama.entity.Comment;
import org.dama.repository.CommentRepository;
import org.dama.service.exception.CommentException;
import org.dama.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Profile("server")
@Transactional
public class CommentServiceJPA implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void addComment(Comment comment) throws CommentException {
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new CommentException("Problem inserting comment", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getComments(String game) {
        try {
            // Predpokladáme, že CommentRepository obsahuje metódu:
            // List<Comment> findByGameOrderByCommentedOnDesc(String game);
            return commentRepository.findByGameOrderByCommentedOnDesc(game);
        } catch (Exception e) {
            throw new CommentException("Problem retrieving comments", e);
        }
    }

    @Override
    public void reset() throws CommentException {
        try {
            commentRepository.deleteAll();
        } catch (Exception e) {
            throw new CommentException("Problem deleting comments", e);
        }
    }
}
