package com.app.chatserver.services;

import java.io.IOException;
import java.util.Optional;

import com.app.chatserver.dto.ResponseChatRoom;
import com.app.chatserver.dto.ResponseChatRoomList;
import com.app.chatserver.models.ChatRoom;
import com.app.chatserver.models.DeletedChatRoom;
import com.app.chatserver.models.User;

public interface ChatRoomService {
	/**
	 * Returns a created chat room.
	 *
	 * @return the chat room
	 */
	ChatRoom createChatRoomForPeerChat();
	/**
	 * Returns a chat room wrapped in optional.
	 *
	 * @param  chatId Integer chatId
	 * @return the chat room
	 */
	ChatRoom findChatRoomById(Integer chatId);
	/**
	 * Returns a list of chat rooms, found by user id
	 *
	 * @param  userId Integer userId
	 * @return the list of chat rooms
	 */
	ResponseChatRoomList findChatRoomsByUserIdAndSerialize(Integer userId, Integer page, Integer pageSize) throws IOException;

	ResponseChatRoom serializeChatRoomToSend(ChatRoom chatRoom, Integer userId) throws IOException;

	/**
	 * moves the chat room to a remote repository
	 *
	 * @param  chatRoom Chat that has been deleted
	 * @param  user He deleted the chat
	 * @return true if chat room exists else return false
	 */
	DeletedChatRoom moveChatRoomToDeletedByChatRoomAndUser(ChatRoom chatRoom, User user);

	DeletedChatRoom findDeletedChatRoomByChatRoomIdAndUserId(Integer chatRoomId, Integer userId);

	boolean existsDeletedChatRoomByChatRoom_IdAndUser_Id(Integer chatRoomId, Integer userId);

	/**
	 * If the user writes to the remote chat room again, then it is restored
	 *
	 * @param  deletedChatRoomId Chat that has been deleted
	 */
	void removeDeletedChatRoomById(Integer deletedChatRoomId);

	ChatRoom createChatRoomForGroupChat(String chatName, String imageBase64);
}
