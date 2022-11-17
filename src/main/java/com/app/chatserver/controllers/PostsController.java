package com.app.chatserver.controllers;

import com.app.chatserver.dto.*;
import com.app.chatserver.services.JwtTokenService;
import com.app.chatserver.services.PostLayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class PostsController {
    private final PostLayerService postLayerService;
    private final JwtTokenService jwtTokenService;

    public PostsController(PostLayerService postLayerService, JwtTokenService jwtTokenService){
        this.jwtTokenService = jwtTokenService;
        this.postLayerService = postLayerService;
    }

    @PostMapping("/post/v1.0/sendPost/{accessToken}")
    public ResponseEntity<PostNotification> newPost(@RequestBody InputPost inputPost,
                                                    @PathVariable String accessToken) throws IOException {
        return new ResponseEntity<>(
            postLayerService.newPost(
                    inputPost,
                    (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")
            ),
            HttpStatus.OK
        );
    }

    @GetMapping("/post/v1.0/editPost/{accessToken}/{postId}")
    public ResponseEntity<Response> editPost( @PathVariable String accessToken,
                                              @PathVariable Integer postId,
                                              @RequestParam InputPost inputPost ) throws IOException {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.editPost(
                        inputPost,
                        postId,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")
                ),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/userPosts/{accessToken}")
    public ResponseEntity<ResponsePostList> getPostsByUserId( @PathVariable String accessToken,
                                                              @RequestParam(defaultValue = "0", required = false) Integer page,
                                                              @RequestParam(defaultValue = "10", required = false) Integer pageSize) throws IOException {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
               postLayerService.findUserPosts(
                       (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id"),
                       page,
                       pageSize
                    ),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/findUserPosts/{accessToken}")
    public ResponseEntity<ResponsePostList> getPostsByUsername( @PathVariable String accessToken,
                                                                @RequestParam String userName,
                                                                @RequestParam(defaultValue = "0", required = false) Integer page,
                                                                @RequestParam(defaultValue = "10", required = false) Integer pageSize) throws IOException {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.findUserPostsByUserName(userName, page, pageSize),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/like/{accessToken}/{postId}")
    public ResponseEntity<Response> likePost( @PathVariable Integer postId,
                                              @PathVariable String accessToken ) {
        jwtTokenService.validateToken(accessToken);
        //TODO send notification to user
        return new ResponseEntity<>(
                postLayerService.likePost(
                         postId,
                         (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/delete/{accessToken}/{postId}")
    public ResponseEntity<Response> deletePost( @PathVariable Integer postId,
                                                @PathVariable String accessToken) {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.deletePost(
                        postId,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/getLiked/{accessToken}")
    public ResponseEntity<ResponsePostList> getLikedPosts( @PathVariable String accessToken,
                                                           @RequestParam(defaultValue = "0", required = false) Integer page,
                                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize ) throws IOException {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService
                        .getLikedPosts(
                                (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id"),
                                page,
                                pageSize
                        ),
                HttpStatus.OK
        );
    }

    @GetMapping("/post/v1.0/unlike/{accessToken}/{postId}")
    public ResponseEntity<Response> unLikePost( @PathVariable Integer postId,
                                                @PathVariable String accessToken ) {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.unLikePost(
                        postId,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/subscribe/{accessToken}/{userName}")
    public ResponseEntity<Response> subscribeOnUser( @PathVariable String userName,
                                                     @PathVariable String accessToken ) {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.subscribeOnUser(
                        userName,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/unsubscribe/{accessToken}/{userName}")
    public ResponseEntity<Response> unsubscribeOnUser( @PathVariable String userName,
                                                       @PathVariable String accessToken ){
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.unsubscribeOnUser(
                        userName,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/feed/{accessToken}")
    public ResponseEntity<ResponsePostList> getFeed( @PathVariable String accessToken,
                                                     @RequestParam(defaultValue = "0", required = false) Integer page,
                                                     @RequestParam(defaultValue = "10", required = false) Integer pageSize ) throws IOException {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.getUserFeed(
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id"),
                        page,
                        pageSize),
                HttpStatus.OK);
    }
    //TODO test
    @PostMapping("/post/v1.0/comment/{accessToken}")
    public ResponseEntity<Response> leaveComment( @PathVariable String accessToken,
                                                  @RequestBody InputComment inputComment ) {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.leaveComment(
                        inputComment,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK);
    }
    //TODO test
    @GetMapping("/post/v1.0/likeComment/{accessToken}/{commentId}")
    public ResponseEntity<Response> likeComment(@PathVariable String accessToken,
                                                 @PathVariable Integer commentId) {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.likeComment(
                        commentId,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/getComments/{accessToken}")
    public ResponseEntity<ResponseCommentList> getComments(@PathVariable String accessToken,
                                                @RequestParam Integer postId,
                                                @RequestParam(defaultValue = "0", required = false) Integer page,
                                                @RequestParam(defaultValue = "10", required = false) Integer pageSize) throws IOException {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.getCommentsByPostId(
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id"),
                        postId,
                        page,
                        pageSize),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/getComment/{accessToken}")
    public ResponseEntity<ResponseComment> getComment(@PathVariable String accessToken,
                                                      @RequestParam Integer commentId) throws IOException {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.getCommentById(
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id"),
                        commentId),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/toBlack/{accessToken}/{bannedUserName}")
    public ResponseEntity<Response> moveUserToBlackList( @PathVariable String accessToken,
                                                         @PathVariable String bannedUserName ) {
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.moveUserToBlackList(
                        bannedUserName,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK);
    }

    @GetMapping("/post/v1.0/getUserInfo/{accessToken}/{userName}")
    public ResponseEntity<ResponseUser> getUserInfo(@PathVariable String accessToken,
                                                    @PathVariable String userName ) throws IOException {
        //TODO check user access
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.getUserInfo(userName),
                HttpStatus.OK
        );
    }

    @GetMapping("/post/v1.0/postById/{accessToken}/{postId}")
    public ResponseEntity<ResponsePost> getPostById(@PathVariable String accessToken,
                                                    @PathVariable Integer postId ) throws IOException {
        //TODO check user access
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.getPostById(
                        postId,
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK
        );
    }

    @GetMapping("/post/v1.0/getSubscribers/{accessToken}")
    public ResponseEntity<List<ResponseSubscriber>> getSubscribers(@PathVariable String accessToken) throws IOException {
        //TODO check user access
        jwtTokenService.validateToken(accessToken);
        return new ResponseEntity<>(
                postLayerService.getSubscribers(
                        (Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")),
                HttpStatus.OK
        );
    }

}
