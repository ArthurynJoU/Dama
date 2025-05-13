package org.dama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.dama.entity.Comment;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByGameOrderByCommentedOnDesc(String game);
}
