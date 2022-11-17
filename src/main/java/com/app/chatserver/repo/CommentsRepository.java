package com.app.chatserver.repo;

import com.app.chatserver.models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findById(Integer aLong);

    List<Comment> findCommentsByPost_Id(Integer postId);

    List<Comment> findTop50ByPost_Id(Integer postId);

    List<Comment> findAllByPost_Id(Integer postId, Pageable pageable);
}
