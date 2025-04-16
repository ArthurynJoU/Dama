package org.dama;

import org.dama.entity.Comment;
import org.dama.repository.CommentRepository;
import org.dama.service.CommentException;
import org.dama.service.CommentServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceJPATest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceJPA commentService;

    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        comment = new Comment("testGame", "testPlayer", "testComment", new Date());
    }

    @Test
    void addComment_ShouldCallSave() throws CommentException {
        commentService.addComment(comment);
        verify(commentRepository, times(1)).save(comment);
    }


    @Test
    void reset_ShouldCallDeleteAll() throws CommentException {
        commentService.reset();
        verify(commentRepository, times(1)).deleteAll();
    }
}
