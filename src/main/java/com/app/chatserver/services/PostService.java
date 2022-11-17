package com.app.chatserver.services;

import com.app.chatserver.dto.InputPost;
import com.app.chatserver.dto.ResponsePost;
import com.app.chatserver.models.Post;
import com.app.chatserver.models.User;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostService {
    String MESSAGE_POST_SUCCESSFULLY_DELETED = "POST_SUCCESSFULLY_DELETED";
    /**
     * save post in repository
     *
     * @param  post - to save
     * @return ResponsePostList
     */
    Post create(Post post);
    /**
     * serialize post to save: create content and save in repository, create media and save repository
     *
     * @param  inputPost - post which user send
     * @param  owner - user who send post
     * @return serialized post
     */
    Post serializePostAndSaveMedia(InputPost inputPost, User owner) throws IOException;
    /**
     * serialize post: create ResponseContent, create ResponseMedia, create ResponsePost
     *
     * @param  post - post which user send
     * @return ResponsePost
     */
    ResponsePost serializePostToResponse(Post post, Integer userId) throws IOException;

    Post findPostById(Integer postId);

    void updateLikeCounter(Integer postId);
    void decLikeCounter(Integer postId);
    String deletePost(Post post);

    boolean checkUserAccess(Integer postId, Integer userId);

    List<Post> getFeedByUserIdPageable(Integer userId, Integer page, Integer pageSize);

    void updateCommentCounter(Integer postId);

    List<Post> findAllByOwnerIdPageable(Integer ownerId, Integer page, Integer pageSize);
}
