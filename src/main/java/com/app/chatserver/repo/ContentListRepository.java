package com.app.chatserver.repo;

import com.app.chatserver.models.ContentList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentListRepository extends JpaRepository<ContentList, Integer> {
    List<ContentList> findContentListByContent_Id(Integer contentId);
}
