package com.app.chatserver.services;

import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.PostException;
import com.app.chatserver.models.LikedPost;
import com.app.chatserver.models.Post;
import com.app.chatserver.models.User;
import com.app.chatserver.repo.LikedPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LikedPostsServiceImpl implements LikedPostsService{
    private final LikedPostRepository likedPostRepository;

    public LikedPostsServiceImpl(LikedPostRepository likedPostRepository){
        this.likedPostRepository = likedPostRepository;
    }

    @Override
    public LikedPost findLikedPostById(Integer id) {
        return likedPostRepository.findById(id).orElseThrow(
                () -> new PostException(ServerChatErrors.POST_NOT_FOUND.name())
        );
    }

    @Override
    public LikedPost findLikedPostByUserIdAndPostId(Integer postId, Integer userId) {
        return likedPostRepository.findLikedPostByPost_IdAndUser_Id(postId, userId).orElseThrow(
                () -> new PostException(ServerChatErrors.POST_NOT_FOUND.name())
        );
    }

    @Override
    public List<LikedPost> findLikedPostsByUserId(Integer userId, Integer page, Integer pageSize) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by("id").descending());
        return likedPostRepository.findAllByUser_Id(userId, paging);
    }

    @Override
    public List<LikedPost> findLikedPostsByPostId(Integer postId) {
        return likedPostRepository.findLikedPostsByPost_Id(postId);
    }

    @Override
    public void save(LikedPost likedPost) {
        likedPostRepository.save(likedPost);
    }

    @Override
    public void create(Post post, User user) {
        if (likedPostRepository.existsByPost_IdAndUser_Id(post.getId(), user.getId())) {
            throw new PostException(ServerChatErrors.ALREADY_LIKED.name());
        }
        LikedPost likedPost = new LikedPost();
        likedPost.setPost(post);
        likedPost.setUser(user);
        likedPostRepository.save(likedPost);
    }

    @Override
    public String deleteLikedPost(LikedPost likedPost) {
        likedPostRepository.delete(likedPost);
        return MESSAGE_SUCCESSFULLY_DELETE_LIKED_POST;
    }

    @Override
    public boolean isPostLikedByUser(Integer postId, Integer userId) {
        return likedPostRepository.existsByPost_IdAndUser_Id(postId, userId);
    }

    @Override
    public Integer likeCountOnPost(Integer postId) {
        return likedPostRepository.findAllByPost_Id(postId).size();
    }
}
