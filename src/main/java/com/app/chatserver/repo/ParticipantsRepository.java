package com.app.chatserver.repo;

import com.app.chatserver.models.ChatRoom;
import com.app.chatserver.models.Participant;
import com.app.chatserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantsRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findParticipantsByChatRoom_Id(Integer chatRoomId);
    List<Participant> findParticipantsByUser(User u);

    @Query(value = "Select * from participiants where user_id = :userId and chat_room_id NOT IN (select chat_room_id from deleted_chat_rooms where user_id = :userId) order by id DESC limit :limit offset :offset", nativeQuery = true)
    List<Participant> findAllByUserId(@Param("userId") Integer userId, @Param("limit") Integer limit, @Param("offset") Integer offset);
    List<Participant> findParticipantsByUser_Id(Integer userId);
    //TODO есть баг если написать самому себе

    @Query(value = "Select * from participiants where user_id = :senderId and chat_room_id = any (select chat_room_id from participiants where user_id = :recipientId)", nativeQuery = true)
    Optional<Participant> findParticipantBySenderIdAndRecipientId(@Param("senderId") Integer senderId, @Param("recipientId") Integer recipientId);

    boolean existsParticipantByChatRoom_IdAndUser_Id(Integer chatRoomId, Integer userId);
}
