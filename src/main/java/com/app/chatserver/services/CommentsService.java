package com.app.chatserver.services;

import com.app.chatserver.dto.InputComment;
import com.app.chatserver.dto.Response;
import com.app.chatserver.dto.ResponseComment;
import com.app.chatserver.dto.ResponseCommentList;
import com.app.chatserver.models.Comment;
import com.app.chatserver.models.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CommentsService {
    Comment findCommentById(Integer commentId);

    Comment serializeAndSave(InputComment inputComment, User owner);

    void save(Comment comment);

    List<Comment> findAllCommentsByPostIdPageable(Integer postId, Integer page, Integer pageSize);

    ResponseCommentList serializeCommentsToResponse(List<Comment> commentList, Integer userId) throws IOException;

    ResponseComment serializeCommentToResponse(Comment comment) throws IOException;
}
