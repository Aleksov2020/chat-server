package com.app.chatserver.repo;

import com.app.chatserver.models.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlackListRepository extends JpaRepository<BlackList, Integer> {
    List<BlackList> findBlackListsByUser_Id(Integer userId);
}
