package com.app.chatserver.repo;

import com.app.chatserver.models.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List <Post> findTop50ByOwner_Id(Integer ownerId);

    @Query(value = "Select posts.id, posts.owner_id, posts.content_id, posts.media_id, posts.like_counter, posts.comment_counter, posts.date_create from posts where owner_id = any (Select user_id from subscriber_list where subscriber_id = :id)  order by id DESC limit :limit offset :offset", nativeQuery = true)
    List<Post> findPostsByUserId(@Param("id") Integer id, @Param("offset") Integer offset,  @Param("limit") Integer limit);

    List<Post> findAllByOwner_Id(Integer ownerId, Pageable pageable);
}
