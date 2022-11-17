package com.app.chatserver.services;

import com.app.chatserver.dto.InputComment;
import com.app.chatserver.dto.ResponseComment;
import com.app.chatserver.dto.ResponseCommentList;
import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.CommentsException;
import com.app.chatserver.models.Comment;
import com.app.chatserver.models.User;
import com.app.chatserver.repo.CommentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
    private final ContentService contentService;
    private final PostService postService;
    private final FileService fileService;

    public CommentsServiceImpl(CommentsRepository commentsRepository,
                               ContentService contentService,
                               PostService postService, FileService fileService){
        this.commentsRepository = commentsRepository;
        this.contentService = contentService;
        this.postService = postService;
        this.fileService = fileService;
    }


    @Override
    public Comment findCommentById(Integer commentId) {
        return commentsRepository.findById(commentId).orElseThrow(
                () -> new CommentsException(ServerChatErrors.COMMENT_NOT_FOUND.name())
        );
    }

    @Override
    public Comment serializeAndSave(InputComment inputComment, User user) {
        Comment comment = new Comment();
        comment.setLikeCount(0l);
        comment.setContent(
                contentService.create(inputComment.getContent())
        );
        comment.setDateCreate(new Date(System.currentTimeMillis()));
        comment.setDateUpdate(new Date(System.currentTimeMillis()));
        comment.setOwner(user);
        if (inputComment.getForwardId() != null) {
            comment.setForward(findCommentById(inputComment.getForwardId()));
        } else {
            comment.setForward(null);
        }
        comment.setPost(
                postService.findPostById(inputComment.getPostId())
        );
        return commentsRepository.save(comment);
    }

    @Override
    public void save(Comment comment) {
        commentsRepository.save(comment);
    }

    @Override
    public List<Comment> findAllCommentsByPostIdPageable(Integer postId, Integer page, Integer pageSize) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by("id").descending());
        return commentsRepository.findAllByPost_Id(postId, paging);
    }

    @Override
    public ResponseCommentList serializeCommentsToResponse(List<Comment> commentList, Integer userId) throws IOException {
        List<ResponseComment> responseCommentList = new ArrayList<>();
        for (Comment comment: commentList){
            responseCommentList.add(
                    serializeCommentToResponse(
                            comment,
                            userId
                    )
            );
        }

        return new ResponseCommentList(
                responseCommentList
        );
    }

    @Override
    public ResponseComment serializeCommentToResponse(Comment comment, Integer userId) throws IOException {
        Integer forwardCommentId;
        if (comment.getForward() != null) {
            forwardCommentId = comment.getForward().getId();
        } else {
            forwardCommentId = null;
        }
        return new ResponseComment(
                comment.getId(),
                comment.getOwner().getUserName(),
                fileService.imageToBase64(comment.getOwner().getAvatar().getPath()),
                contentService.serializeContentToSend(comment.getContent()),
                forwardCommentId,
                comment.getLikeCount(),
                comment.getDateCreate(),
                comment.getDateUpdate(),

        );
    }
}
