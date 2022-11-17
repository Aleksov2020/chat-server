package com.app.chatserver.repo;

import com.app.chatserver.models.DeletedChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeletedChatRoomRepository extends JpaRepository<DeletedChatRoom, Integer> {
    List<DeletedChatRoom> findDeletedChatRoomsByUserId(Integer senderId);

    boolean existsByChatRoom_IdAndUser_Id(Integer chatRoomId, Integer userId);

    Optional<DeletedChatRoom> findByChatRoom_IdAndUser_Id(Integer chatRoomId, Integer userId);
    //TODO тут чето страшное
    void deleteById(Integer deletedChatRoomId);
}
