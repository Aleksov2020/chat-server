package com.app.chatserver.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.app.chatserver.dto.ResponseChatRoom;
import com.app.chatserver.dto.ResponseChatRoomList;
import com.app.chatserver.dto.ResponseParticipants;
import com.app.chatserver.enums.ChatType;
import com.app.chatserver.enums.MediaType;
import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.ChatRoomException;
import com.app.chatserver.models.*;
import com.app.chatserver.repo.DeletedChatRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.chatserver.repo.ChatRoomRepository;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
	private final ChatRoomRepository chatRoomRepository;
	private final ParticipantsService participantsService;
	private final MediaService mediaService;
	private final DeletedChatRoomRepository deletedChatRoomRepository;
	private final FileService fileService;
	@Autowired
	public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository,
							   ParticipantsService participantsService,
							   MediaService mediaService,
							   DeletedChatRoomRepository deletedChatRoomRepository,
							   FileService fileService){
		this.chatRoomRepository = chatRoomRepository;
		this.participantsService = participantsService;
		this.mediaService = mediaService;
		this.deletedChatRoomRepository = deletedChatRoomRepository;
		this.fileService = fileService;
	}

	@Override
	public ChatRoom createChatRoomForPeerChat() {
		Media savedMedia = mediaService.createAndSaveMedia("PeerChat", MediaType.image, 0L, "");
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setDateStart(new Date(System.currentTimeMillis()));
		chatRoom.setDateLastUpdate(new Date(System.currentTimeMillis()));
		chatRoom.setChatType(ChatType.peer);
		chatRoom.setMedia(savedMedia);
		chatRoomRepository.save(chatRoom);
		return chatRoom;
	}
	@Override
	public ChatRoom findChatRoomById(Integer id) {
		return chatRoomRepository.getChatRoomById(id).orElseThrow(
				() -> new ChatRoomException(ServerChatErrors.CHAT_ROOM_NOT_FOUND.name())
		);
	}
	@Override
	public ResponseChatRoomList findChatRoomsByUserIdAndSerialize(Integer userId, Integer page, Integer pageSize) throws IOException {
		//TODO check deleted chatRooms
		//TODO lamda
		List<ResponseChatRoom> responseChatRoomList = new ArrayList<>();
		for (Participant participant : participantsService.findAllByUserId(userId, page, pageSize)) {
			List <User> userList = participantsService.findListOfParticipantInChat(userId, participant.getChatRoom().getId());
			List <ResponseParticipants> responseParticipantsList = new ArrayList<>();
			for (User u : userList) {
				responseParticipantsList.add(
						new ResponseParticipants(
								u.getUserName(),
								fileService.imageToBase64(u.getAvatar().getPath())
						)
				);
			}
			responseChatRoomList.add(new ResponseChatRoom(
					participant.getChatRoom().getId(),
					participant.getChatRoom().getChatName(),
					null,
					participant.getChatRoom().getDateLastUpdate(),
					responseParticipantsList
			));
		}
		return new ResponseChatRoomList(
				responseChatRoomList
		);
	}

	@Override
	public DeletedChatRoom findDeletedChatRoomByChatRoomIdAndUserId(Integer chatRoomId, Integer userId) {
		return deletedChatRoomRepository.findByChatRoom_IdAndUser_Id(chatRoomId, userId).orElseThrow(
				() -> new ChatRoomException(ServerChatErrors.CHAT_ROOM_NOT_FOUND.name())
		);
	}

	@Override
	public boolean existsDeletedChatRoomByChatRoom_IdAndUser_Id(Integer chatRoomId, Integer userId) {
		return deletedChatRoomRepository.existsByChatRoom_IdAndUser_Id(chatRoomId, userId);
	}

	@Override
	public void removeDeletedChatRoomById(Integer deletedChatRoomId) {
		//TODO checkChatRoomExists
		deletedChatRoomRepository.deleteById(deletedChatRoomId);
	}

	@Override
	public ChatRoom createChatRoomForGroupChat(String chatName, String imageBase64) {
		ChatRoom chatRoom = new ChatRoom();

		chatRoom.setChatType(ChatType.group);
		chatRoom.setChatName(chatName);
		chatRoom.setDateStart(new Date(System.currentTimeMillis()));
		chatRoom.setDateLastUpdate(new Date(System.currentTimeMillis()));

		return chatRoom;
	}

	@Override
	public ResponseChatRoom serializeChatRoomToSend(ChatRoom chatRoom, Integer userId) throws IOException {
		List <User> userList = participantsService.findListOfParticipantInChat(userId, chatRoom.getId());
		List <ResponseParticipants> responseParticipantsList = new ArrayList<>();

		for (User u : userList) {
			responseParticipantsList.add(
					new ResponseParticipants(
							u.getUserName(),
							fileService.imageToBase64(u.getAvatar().getPath())
					)
			);
		}
		return new ResponseChatRoom(
				chatRoom.getId(),
				chatRoom.getChatName(),
				null, //TODO set unnull
				chatRoom.getDateLastUpdate(),
				responseParticipantsList
		);
	}

	@Override
	public DeletedChatRoom moveChatRoomToDeletedByChatRoomAndUser(ChatRoom chatRoom, User user) {
		DeletedChatRoom deletedChatRoom = new DeletedChatRoom();
		deletedChatRoom.setChatRoom(chatRoom);
		deletedChatRoom.setUser(user);
		return deletedChatRoomRepository.save(deletedChatRoom);
	}
}
