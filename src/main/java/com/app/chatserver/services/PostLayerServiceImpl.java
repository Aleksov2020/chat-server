package com.app.chatserver.services;

import com.app.chatserver.dto.*;
import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.CommentsException;
import com.app.chatserver.exceptions.PostException;
import com.app.chatserver.exceptions.SubscribeException;
import com.app.chatserver.exceptions.UserException;
import com.app.chatserver.models.Comment;
import com.app.chatserver.models.LikedPost;
import com.app.chatserver.models.Post;
import com.app.chatserver.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class PostLayerServiceImpl implements PostLayerService{
    private final MessageBrokerService messageBrokerService;
    private final UserService userService;
    private final PostService postService;
    private final LikedPostsService likedPostsService;
    private final SubscriberListService subscriberListService;
    private final BlackListService blackListService;
    private final CommentsService commentsService;
    private final FileService fileService;

    public PostLayerServiceImpl(MessageBrokerService messageBrokerService,
                                UserService userService,
                                PostService postService,
                                LikedPostsService likedPostsService,
                                SubscriberListService subscriberListService,
                                CommentsService commentsService,
                                BlackListService blackListService,
                                FileService fileService){
        this.postService = postService;
        this.messageBrokerService = messageBrokerService;
        this.userService = userService;
        this.likedPostsService = likedPostsService;
        this.subscriberListService = subscriberListService;
        this.commentsService = commentsService;
        this.blackListService = blackListService;
        this.fileService = fileService;
    }

    @Override
    public PostNotification newPost(InputPost inputPost, Integer ownerId) throws IOException {
        PostNotification postNotification = new PostNotification(
                postService.create(
                        postService.serializePostAndSaveMedia(
                                inputPost,
                                userService.findUserById(ownerId)
                        )
                ).getId(),
                userService.findUserById(
                        ownerId
                ).getUserName()
        );

        messageBrokerService.sendToUserPostNotification(
                userService.findUserById(ownerId).getUserName(),
                postNotification
        );
        userService.incPostsCounter(userService.findUserById(ownerId));

        return postNotification;
    }


    @Override
    public ResponsePostList findUserPosts(Integer ownerId, Integer page, Integer pageSize) throws IOException {
        //TODO foreach
        ResponsePostList responsePostList = new ResponsePostList();
        for(Post post: postService.findAllByOwnerIdPageable(ownerId, page, pageSize)) {
            responsePostList.getResponsePostList().add(
                    postService.serializePostToResponse(
                            post,
                            ownerId
                    )
            );
        }
        return responsePostList;
    }

    @Override
    public ResponsePostList findUserPostsByUserName(String userName, Integer page, Integer pageSize) throws IOException {
        //TODO foreach
        ResponsePostList responsePostList = new ResponsePostList();
        for(Post post: postService.findAllByOwnerIdPageable(userService.findUserByUserName(userName).getId(), page, pageSize)) {
            responsePostList.getResponsePostList().add(
                    postService.serializePostToResponse(
                            post,
                            userService.findUserByUserName(userName).getId()
                    )
            );
        }
        return responsePostList;
    }

    @Override
    public Response likePost(Integer postId, Integer userId) {
        //TODO ? check user access to like
        likedPostsService.create(
                postService.findPostById(postId),
                userService.findUserById(userId)
        );
        postService.updateLikeCounter(postId);
        return new Response(
                MESSAGE_SUCCESSFULLY_LIKE
        );
    }

    @Override
    public ResponsePostList getLikedPosts(Integer userId, Integer page, Integer pageSize) throws IOException {
        List<LikedPost> likedPostList = likedPostsService.findLikedPostsByUserId(userId, page, pageSize);
        List<ResponsePost> responsePostList = new ArrayList<>();

        if (likedPostList.size() == 0) {
            throw new PostException(ServerChatErrors.LIKED_POSTS_NOT_FOUND.name());
        }

        for (LikedPost likedPost : likedPostList) {
            responsePostList.add(
                    postService.serializePostToResponse(
                            likedPost.getPost(),
                            userId
                    )
            );
        }

        return new ResponsePostList(
                responsePostList
        );
    }

    @Override
    public Response deletePost(Integer postId, Integer userId) {
        if (!postService.checkUserAccess(postId, userId)) {
            throw new PostException(ServerChatErrors.ACCESS_DENIED.name());
        }
        userService.decPostsCounter(
                userService.findUserById(userId)
        );
        return new Response(
                postService.deletePost(
                        postService.findPostById(postId)
                )
        );
    }

    @Override
    public Response unLikePost(Integer postId, Integer userId) {
        //TODO check if user already unlike this
        postService.decLikeCounter(postId);
        return new Response(
            likedPostsService.deleteLikedPost(
                    likedPostsService.findLikedPostByUserIdAndPostId(postId, userId)
            )
        );
    }

    @Override
    public Response subscribeOnUser(String userName, Integer id) {
        return new Response(
                subscriberListService.createSubscribe(
                        userService.findUserById(id),
                        userService.findUserByUserName(userName)
                )
        );
    }

    @Override
    public Response unsubscribeOnUser(String userName, Integer id) {
        return new Response(
                subscriberListService.deleteSubscribe(
                        subscriberListService.findSubscriberListBySubscriberAndUser(
                                userService.findUserById(id),
                                userService.findUserByUserName(userName)
                        )
                )
        );
    }
    @Override
    public ResponsePostList getUserFeed(Integer userId, Integer page, Integer pageSize) throws IOException {
        Collection<Post> postList = postService.getFeedByUserIdPageable(userId, page, pageSize);
        List<ResponsePost> responsePostList = new ArrayList<>();
        if (postList.size() == 0) {
            throw new SubscribeException(ServerChatErrors.FEED_NOT_FOUND.name());
        }

        for (Post post : postList) {
            responsePostList.add(
                    postService.serializePostToResponse(
                            post,
                            userId
                    )
            );
        }
        return new ResponsePostList(
                responsePostList
        );
    }

    @Override
    public Response leaveComment(InputComment inputComment, Integer userId) {
        commentsService.serializeAndSave(
                inputComment,
                userService.findUserById(userId)
        );
        postService.updateCommentCounter(inputComment.getPostId());
        return new Response(
                MESSAGE_COMMENT_SEND_SUCCESSFULLY
        );
    }

    @Override
    public Response likeComment(Integer commentId, Integer id) {
        Comment comment = commentsService.findCommentById(commentId);
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentsService.save(comment);
        return new Response(
                MESSAGE_COMMENT_LIKE_SUCCESSFULLY
        );
    }

    @Override
    public Response editPost(InputPost inputPost, Integer postId, Integer id) throws IOException {
        Post oldPost = postService.findPostById(postId);
        if (!oldPost.getOwner().getId().equals(postId)) {
            throw new PostException(ServerChatErrors.ACCESS_DENIED.name());
        }
        postService.serializePostAndSaveMedia(
                inputPost,
                userService.findUserById(id)
        );
        return new Response(
                MESSAGE_SUCCESSFULLY_EDITED_POST
        );
    }

    @Override
    public Response moveUserToBlackList(String bannedUserName, Integer id) {
        blackListService.create(
                userService.findUserByUserName(bannedUserName),
                userService.findUserById(id)
        );
        return new Response(
                MESSAGE_USER_SUCCESSFULLY_MOVED_TO_BLACK_LIST
        );
    }

    @Override
    public ResponseUser getUserInfo(String userName) throws IOException {
        User u = userService.findUserByUserName(userName);
        return new ResponseUser(
                new ResponseMedia(
                        fileService.imageToBase64(u.getAvatar().getPath())
                ),
                u.getUserName(),
                u.getFirstName(),
                u.getMiddleName(),
                u.getLastName(),
                u.getPostsCounter(),
                u.getSubscriberCounter(),
                u.getSubscribedCounter(),
                u.getDateLastEnter(),
                u.getDateLastUpdate(),
                u.getDateCreate(),
                u.getIsActive(),
                u.getIsReported(),
                u.getIsBanned(),
                null //TODO need to write
        );
    }

    @Override
    public ResponseCommentList getCommentsByPostId(Integer id, Integer postId, Integer page, Integer pageSize) throws IOException {
        //TODO check Access
        return commentsService.serializeCommentsToResponse(
                commentsService.findAllCommentsByPostIdPageable(postId, page, pageSize),
                id
        );
    }

    @Override
    public ResponseComment getCommentById(Integer id, Integer commentId) throws IOException {
        return commentsService.serializeCommentToResponse(
                commentsService.findCommentById(commentId)
        );
    }

    @Override
    public ResponsePost getPostById(Integer postId, Integer userId) throws IOException {
        return postService.serializePostToResponse(
                postService.findPostById(postId),
                userId
        );
    }

    @Override
    public List<ResponseSubscriber> getSubscribers(Integer userId) {
        return subscriberListService.serializeSubscribersToResponse(
                subscriberListService.findUsersThatUserHasSubscribed(
                        userService.findUserById(userId).getId()
                )
        );
    }
}
