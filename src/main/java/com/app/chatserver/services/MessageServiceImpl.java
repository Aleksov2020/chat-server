package com.app.chatserver.services;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.app.chatserver.dto.InputMessage;
import com.app.chatserver.dto.ResponseMedia;
import com.app.chatserver.dto.ResponseMessage;
import com.app.chatserver.dto.ResponseMessageList;
import com.app.chatserver.enums.MediaType;
import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.MediaException;
import com.app.chatserver.exceptions.MessageException;
import com.app.chatserver.exceptions.UserException;
import com.app.chatserver.models.*;
import com.app.chatserver.repo.DeletedMessagesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.app.chatserver.repo.MessageRepository;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
	private final MessageRepository messageRepository;
	private final ContentService contentService;
	private final UserService userService;
	private final DeletedMessagesRepository deletedMessagesRepository;

	private final MediaService mediaService;

	public MessageServiceImpl(MessageRepository messageRepository,
							  ContentService contentService,
							  UserService userService,
							  DeletedMessagesRepository deletedMessagesRepository,
							  MediaService mediaService) {
		this.messageRepository = messageRepository;
		this.contentService = contentService;
		this.userService = userService;
		this.deletedMessagesRepository = deletedMessagesRepository;
		this.mediaService = mediaService;
	}

	@Override
	public Message create(InputMessage inputMessage, ChatRoom chatRoom, User user) throws IOException {
		Message m = new Message();
		m.setChat(chatRoom);
		m.setDateCreate(new Date(System.currentTimeMillis()));
		m.setContent(contentService.create(inputMessage.getContent()));
		//log.info(inputMessage.toString());
		if (inputMessage.getMedia() == null) {
			m.setMedia(null);
		} else {
			if (inputMessage.getMediaType() == null) {
				throw new MessageException(ServerChatErrors.MEDIA_TYPE_REQUIRED.name());
			} else {
				//if (inputMessage.getMediaType() == MediaType.image.name()) {
				log.info("Media type = image");
				m.setMedia(
						mediaService.serializeMediaFromBase64AndSave(
								inputMessage.getMedia(),
								user.getUserName()
						)
				);
				//} else if (inputMessage.getMediaType() == MediaType.audio.name()) {
				//	mediaService.serializeAudioFromBase64AndSave(
				//			inputMessage.getMedia(),
				//			user.getUserName()
				//	);
				//}
			}

		}
		if (inputMessage.getForwardId() != null) {
			m.setForward(findMessageById(inputMessage.getForwardId()));
		} else {
			m.setForward(null);
		}
		m.setSender(user);
		return messageRepository.save(m);
	}

	@Override
	public Message findMessageById(Integer id) {
		return messageRepository.findMessageById(id).orElseThrow(
				() -> new MessageException(ServerChatErrors.MESSAGE_NOT_FOUND.name())
		);
	}

	@Override
	public List<Message> findMessagesByChatId(Integer chatId) {
		return messageRepository.findMessagesByChat_Id(chatId);
	}

	@Override
	public List<Message> findMessagesByChatIdPageable(Integer chatId, Integer page, Integer pageSize) {
		Pageable paging = PageRequest.of(page,pageSize,Sort.by("id").descending());
		return messageRepository.findAllByChat_Id(
				chatId,
				paging
		);
	}

	@Override
	public String moveMessageToDeleted(Message message, User user) {
		//TODO check deleted for all users. if true - delete from Messages Table
		DeletedMessage deletedMessage = new DeletedMessage();
		deletedMessage.setMessage(message);
		deletedMessage.setUser(user);
		deletedMessagesRepository.save(deletedMessage);
		return MESSAGE_SUCCESSFUL_DELETE;
	}

	@Override
	public void moveMessagesToDeleted(List<DeletedMessage> deletedMessageList) {
		deletedMessagesRepository.saveAll(deletedMessageList);
	}

	@Override
	public void moveMessagesToDeletedFromChatRoom(DeletedChatRoom deletedChatRoom) {
		//TODO check if messages exists in DeletedMessages
		List<Message> messageList = this.findMessagesByChatId(deletedChatRoom.getChatRoom().getId());
		List<DeletedMessage> deletedMessageList = new ArrayList<>();
		for (Message message : messageList) {
			DeletedMessage deletedMessage = new DeletedMessage();
			deletedMessage.setMessage(message);
			deletedMessage.setUser(deletedChatRoom.getUser());
			deletedMessageList.add(deletedMessage);
		}
		moveMessagesToDeleted(deletedMessageList);
	}

	@Override
	public boolean checkInputNewMessage(InputMessage inputMessage) {
		return false;
	}

	public ResponseMessageList serializeMessages(List<Message> messageList, Integer userId) throws IOException {
		List<ResponseMessage> responseMessageList = new ArrayList<>();
		for (Message message : messageList){
			if (!deletedMessagesRepository.existsByMessage_IdAndUser_Id(message.getId(), userId)) {
				responseMessageList.add(serializeMessage(message));
			}
		}
		return new ResponseMessageList(responseMessageList);
	}

	@Override
	public ResponseMessage serializeMessage(Message message) throws IOException {
		Integer forwardId;
		ResponseMedia media;
		if (message.getForward() == null){
			forwardId = null;
		} else {
			forwardId = message.getForward().getId();
		}
		if (message.getMedia() == null){
			media = null;
		} else {
			media = mediaService.serializeMediaToSend(
					message.getMedia()
			);
		}
		return new ResponseMessage(
				message.getId(),
				message.getSender().getUserName(),
				message.getChat().getId(),
				contentService.serializeContentToSend(message.getContent()),
				media,
				forwardId,
				message.getDateCreate()
		);
	}
}
