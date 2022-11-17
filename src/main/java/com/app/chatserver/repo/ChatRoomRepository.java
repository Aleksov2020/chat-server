package com.app.chatserver.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.chatserver.models.ChatRoom;

public interface ChatRoomRepository extends JpaRepository <ChatRoom, Integer>{
  Optional<ChatRoom> getChatRoomById(Integer chatId);
  boolean existsById(Integer chatId);


}
