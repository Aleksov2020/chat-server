package com.app.chatserver.services;

import com.app.chatserver.dto.InputPost;
import com.app.chatserver.dto.ResponsePost;
import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.PostException;
import com.app.chatserver.models.Content;
import com.app.chatserver.models.Post;
import com.app.chatserver.models.User;
import com.app.chatserver.repo.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ContentService contentService;
    private final ContentListService contentListService;
    private final MediaService mediaService;
    private final FileService fileService;
    private final LikedPostsService likedPostsService;

    public PostServiceImpl(          PostRepository postRepository,
                                     ContentService contentService,
                                     ContentListService contentListService,
                                     MediaService mediaService,
                                     FileService fileService,
                                     LikedPostsService likedPostsService){
        this.postRepository = postRepository;
        this.contentService = contentService;
        this.contentListService = contentListService;
        this.mediaService = mediaService;
        this.fileService = fileService;
        this.likedPostsService = likedPostsService;
    }

    @Override
    public Post create(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post serializePostAndSaveMedia(InputPost inputPost, User owner) throws IOException {
        Post p = new Post();
        Content content = contentService.create(inputPost.getContent());
        p.setOwner(owner);
        p.setContent(content);
        p.setLikeCounter(0);
        p.setCommentCounter(0);
        p.setDateCreate(new Date(System.currentTimeMillis()));

        if (inputPost.getImageList() != null) {
            for (String image : inputPost.getImageList()) {
                contentListService.create(
                        mediaService.serializeMediaFromBase64AndSave(
                                image,
                                owner.getUserName()
                        ),
                        content
                );
            }
        }

        return p;
    }

    @Override
    public ResponsePost serializePostToResponse(Post post, Integer userId) throws IOException {
        return new ResponsePost(
                post.getId(),
                post.getOwner().getUserName(),
                fileService.imageToBase64(
                        post.getOwner().getAvatar().getPath()
                ),
                contentService.serializeContentToSend(post.getContent()),
                mediaService.serializeMediaToSendInList(contentListService.findMediaListByContent_Id(post.getContent().getId())),
                post.getLikeCounter(),
                likedPostsService.likeCountOnPost(post.getId()),
                likedPostsService.isPostLikedByUser(post.getId(), userId),
                post.getDateCreate()
        );
    }

    @Override
    public Post findPostById(Integer postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostException(ServerChatErrors.POST_NOT_FOUND.name())
        );
    }

    @Override
    public void updateLikeCounter(Integer postId) {
        Post p = postRepository.findById(postId).orElseThrow(
                () -> new PostException(ServerChatErrors.POST_NOT_FOUND.name())
        );
        p.setLikeCounter(p.getLikeCounter() + 1);
        postRepository.save(p);
    }

    @Override
    public void decLikeCounter(Integer postId) {
        Post p = findPostById(postId);
        p.setLikeCounter(p.getLikeCounter() - 1);
        postRepository.save(p);
    }

    @Override
    public String deletePost(Post post) {
        postRepository.delete(post);
        return MESSAGE_POST_SUCCESSFULLY_DELETED;
    }

    @Override
    public boolean checkUserAccess(Integer postId, Integer userId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostException(ServerChatErrors.POST_NOT_FOUND.name())
        ).getOwner().getId().equals(userId);
    }

    @Override
    public List<Post> getFeedByUserIdPageable(Integer userId, Integer page, Integer pageSize) {
        return postRepository.findPostsByUserId(userId, page*pageSize, pageSize);
    }

    @Override
    public void updateCommentCounter(Integer postId) {
        Post p = postRepository.findById(postId).orElseThrow(
                () -> new PostException(ServerChatErrors.POST_NOT_FOUND.name())
        );
        p.setCommentCounter(p.getLikeCounter() + 1);
        postRepository.save(p);
    }

    @Override
    public List<Post> findAllByOwnerIdPageable(Integer ownerId, Integer page, Integer pageSize) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by("id").descending());
        return postRepository.findAllByOwner_Id(ownerId, paging);
    }
}
