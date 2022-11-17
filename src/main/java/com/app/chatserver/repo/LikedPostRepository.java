package com.app.chatserver.repo;

import com.app.chatserver.models.LikedPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikedPostRepository extends JpaRepository<LikedPost, Integer> {
    Optional<LikedPost> findById(Integer id);
    List<LikedPost> findAllByUser_Id(Integer userId, Pageable pageable);
    List<LikedPost> findLikedPostsByPost_Id(Integer userId);


    Optional<LikedPost> findLikedPostByPost_IdAndUser_Id(Integer postId, Integer userId);
    boolean existsByPost_IdAndUser_Id(Integer postId, Integer userId);
    List<LikedPost> findAllByPost_Id(Integer postId);
}
