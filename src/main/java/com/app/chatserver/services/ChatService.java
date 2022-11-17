package com.app.chatserver.services;

import com.app.chatserver.dto.*;
import com.app.chatserver.models.User;

import java.io.IOException;
import java.util.Optional;

public interface ChatService {
    String MESSAGE_SUCCESSFUL_DELETED_CHAT_ROOM = "SUCCESSFUL_DELETED_CHAT_ROOM";
    /**
     * Send message from user
     *
     * @param  inputMessage user's message
     */
    ResponseFirstMessage sendNewMessage(InputMessage inputMessage, Integer senderId) throws IOException;
    /**
     * Get a list of messages by user id and by chat room
     *
     * @param  chatId messages from chat
     * @param  userId who want to get messages
     */
    ResponseMessageList getMessages(Integer chatId, Integer userId, Integer page, Integer pageSize) throws IOException;
    /**
     * move message to deleted for user. In method "getMessages" user
     * can't get this message
     *
     * @param  messageId id deleted message
     * @param  userId who want to delete message
     * @return String message about delete
     */
    String moveMessageToDeletedForUser(Integer messageId, Integer userId);
    /**
     * move all messages in chat room to deleted for user. In method "getMessages" user
     * can't get this messages
     *
     * @param  chatId id deleted chat room
     * @param  userId who want to delete chat room
     * @retutn String message about delete
     */
    String moveChatRoomToDeletedForUser(Integer chatId, Integer userId);

    String getUserNameByPhone(String userName);

    void createGroupChat(InputGroupChat inputGroupChat);

    void sendNewGroupMessage(InputMessage inputMessage, Integer senderId) throws IOException;

    ResponseMedia getUserPhoto(Integer userId);

    ResponseMessage getMessageById(Integer messageId, Integer userId) throws IOException;

    ResponsePin pinImage(String imageBase64, Integer userId) throws IOException;
}
