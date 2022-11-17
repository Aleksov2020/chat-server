package com.app.chatserver.services;

import com.app.chatserver.dto.*;
import com.app.chatserver.models.Post;
import com.app.chatserver.models.User;

import java.io.IOException;
import java.util.List;

public interface PostLayerService {
    String MESSAGE_SUCCESSFULLY_LIKE = "SUCCESSFULLY_LIKE";
    String MESSAGE_COMMENT_SEND_SUCCESSFULLY = "COMMENT_SEND_SUCCESSFULLY";
    String MESSAGE_COMMENT_LIKE_SUCCESSFULLY = "COMMENT_LIKE_SUCCESSFULLY";
    String MESSAGE_SUCCESSFULLY_EDITED_POST = "COMMENT_LIKE_SUCCESSFULLY";

    String MESSAGE_USER_SUCCESSFULLY_MOVED_TO_BLACK_LIST = "USER_SUCCESSFULLY_MOVED_TO_BLACK_LIST";
    /**
     * send post to the user channel and save in repository
     *
     * @param  inputPost - user we are checking in the chat
     * @param  ownerId - peer chat room with two users
     */
    PostNotification newPost(InputPost inputPost, Integer ownerId) throws IOException;
    /**
     * return found user
     *
     * @param  ownerId - user owner of posts
     * @return ResponsePostList
     */
    ResponsePostList findUserPosts(Integer ownerId, Integer page, Integer pageSize) throws IOException;

    ResponsePostList findUserPostsByUserName(String userName, Integer page, Integer pageSize) throws IOException;

    Response likePost(Integer postId, Integer userId);

    ResponsePostList getLikedPosts(Integer userId, Integer page, Integer pageSize) throws IOException;

    Response deletePost(Integer postId, Integer userId);

    Response unLikePost(Integer postId, Integer userId);

    Response subscribeOnUser(String userName, Integer id);

    Response unsubscribeOnUser(String userName, Integer id);

    ResponsePostList getUserFeed(Integer userId, Integer page, Integer pageSize) throws IOException;

    Response leaveComment(InputComment inputComment, Integer userId);

    Response likeComment(Integer commentId, Integer id);

    Response editPost(InputPost inputPost, Integer postId, Integer id) throws IOException;

    Response moveUserToBlackList(String bannedUserName, Integer id);

    ResponseUser getUserInfo(String userName) throws IOException;

    ResponseCommentList getCommentsByPostId(Integer userId, Integer postId, Integer page, Integer pageSize) throws IOException;

    ResponseComment getCommentById(Integer id, Integer commentId) throws IOException;

    ResponsePost getPostById(Integer postId, Integer userId) throws IOException;

    List<ResponseSubscriber> getSubscribers(Integer userId);
}
