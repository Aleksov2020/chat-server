package com.app.chatserver.repo;

import com.app.chatserver.models.DeletedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedMessagesRepository extends JpaRepository<DeletedMessage, Integer> {
    boolean existsByMessage_IdAndUser_Id(Integer messageId, Integer userId);
}
