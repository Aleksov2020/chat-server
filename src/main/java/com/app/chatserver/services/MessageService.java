package com.app.chatserver.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.app.chatserver.dto.InputMessage;
import com.app.chatserver.dto.ResponseMessage;
import com.app.chatserver.dto.ResponseMessageList;
import com.app.chatserver.models.*;

public interface MessageService {
	String MESSAGE_SUCCESSFUL_DELETE = "SUCCESSFUL_DELETE";

	/**
	 * Returns a serialized message to send to the user
	 *
	 * @param  message - Message which need to be serialized
	 * @return the Response Message
	 */
	ResponseMessage serializeMessage(Message message) throws IOException;

	/**
	 * Create message and save in database
	 *
	 * @param  inputMessage - Input Message from user
	 * @param  chatRoom - chat room with this message
	 * @param  user - which sent message
	 * @return the Message
	 */
	Message create(InputMessage inputMessage, ChatRoom chatRoom, User user) throws IOException;

	/**
	 * Returns a Message object wrapped Optional class
	 *
	 * @param  messageId - integer message id
	 * @return the Message
	 */
	Message findMessageById(Integer messageId);

	/**
	 * Returns a list of messages in chat room by chat id
	 *
	 * @param  chatId - integer chat id
	 * @return the List of Messages
	 */
	List<Message> findMessagesByChatId(Integer chatId);

	/**
	 * Returns a list of top 50 messages in chat room by chat id
	 *
	 * @param  chatId - integer chat id
	 * @return the List of Messages
	 */
	List<Message> findMessagesByChatIdPageable(Integer chatId, Integer page, Integer pageSize);

	/**
	 * Returns a list of serialized messages.
	 *
	 * @param  messageList - list of messages which need to be serialized
	 * @param  userId - user which send messages
	 * @return the ResponseMessageList
	 */
	ResponseMessageList serializeMessages(List<Message> messageList, Integer userId) throws IOException;

	/**
	 * Move message to deleted repository.
	 *
	 * @param  message - message which need to be moved to deleted repository
	 * @param  user - user which send messages
	 * @return the message
	 */
	String moveMessageToDeleted(Message message, User user);

	/**
	 * Move message to deleted repository.
	 *
	 * @param  deletedMessageList - message list which need to be moved to deleted repository
	 */
	void moveMessagesToDeleted(List<DeletedMessage> deletedMessageList);

	/**
	 * Move message to deleted repository by deleted chat room.
	 *
	 * @param  deletedChatRoom - from this chat room we move all messages to deleted
	 */
	void moveMessagesToDeletedFromChatRoom(DeletedChatRoom deletedChatRoom);

	/**
	 * check input message by regex
	 *
	 * @param  inputMessage - from this chat room we move all messages to deleted
	 */
	boolean checkInputNewMessage(InputMessage inputMessage);
}
