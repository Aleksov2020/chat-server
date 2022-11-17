package com.app.chatserver.services;

import com.app.chatserver.models.LikedPost;
import com.app.chatserver.models.Post;
import com.app.chatserver.models.User;

import java.util.List;
import java.util.Optional;

public interface LikedPostsService {
    String MESSAGE_SUCCESSFULLY_DELETE_LIKED_POST = "SUCCESSFULLY_DELETE_LIKED_POST";
    LikedPost findLikedPostById(Integer id);
    LikedPost findLikedPostByUserIdAndPostId(Integer postId, Integer userId);
    List<LikedPost> findLikedPostsByUserId(Integer userId, Integer page, Integer pageSize);
    List<LikedPost> findLikedPostsByPostId(Integer postId);
    void save(LikedPost likedPost);
    void create(Post post, User user);
    String deleteLikedPost(LikedPost likedPost);
    boolean isPostLikedByUser(Integer postId, Integer userId);
    Integer likeCountOnPost(Integer postId);
}
