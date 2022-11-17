package com.app.chatserver.services;

import com.app.chatserver.dto.InputGroupChat;
import com.app.chatserver.models.ChatRoom;
import com.app.chatserver.models.Participant;
import com.app.chatserver.models.User;

import java.util.List;

public interface ParticipantsService {
    /**
     * creating chat participants
     *
     * @param  senderId - the user who sent the message
     * @param  recipientId - the user the message was sent to
     * @param  chatRoom - chat room with a new message
     */
    void createPeerChat(Integer senderId, Integer recipientId, ChatRoom chatRoom);
    /**
     * check if user is in chat
     *
     * @param  userId - the user we are checking in the chat
     * @param  recipientId - the chat
     * @return true if user exists else return false
     */
    boolean existsParticipantForPeerChat(Integer userId, Integer recipientId);
    /**
     * return list of participants in a chat room
     *
     * @param  chatRoomId - the user we are checking in the chat
     * @return list of participants
     */
    List<Participant> findParticipantsByChatRoomId(Integer chatRoomId);
    /** TODO CHETO TYT STRANNOE
     * return top 50 chat participants by user_id
     *
     * @param  userId - the user we are checking in the chat
     * @return list of participants
     */
    List<Participant> findAllByUserId(Integer userId, Integer page, Integer pageSize);
    /**
     * return found user
     *
     * @param  senderId - user we are checking in the chat
     * @param  chatRoom - peer chat room with two users
     * @return user
     */
    User findRecipient(Integer senderId, ChatRoom chatRoom);

    List<Participant> createGroupChat(List<String> membersNameList, ChatRoom chatRoom);

    List<User> findListOfParticipantInChat(Integer userId, Integer chatId);

    Participant findParticipantBySenderIdAndRecipientId(Integer senderId, Integer recipientId);
    boolean existsParticipantByChatIdAndUserId(Integer chatId, Integer userId);


}