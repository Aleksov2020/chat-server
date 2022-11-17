package com.app.chatserver.repo;

import com.app.chatserver.models.Content;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Integer> {
    List<Content> findContentByContentContains(String text, Pageable pageable);
}
