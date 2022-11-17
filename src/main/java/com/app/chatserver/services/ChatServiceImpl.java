package com.app.chatserver.services;

import com.app.chatserver.dto.*;
import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.ChatRoomException;
import com.app.chatserver.exceptions.MessageException;
import com.app.chatserver.exceptions.ParticipantException;
import com.app.chatserver.exceptions.UserException;
import com.app.chatserver.models.ChatRoom;
import com.app.chatserver.models.DeletedChatRoom;
import com.app.chatserver.models.Message;
import com.app.chatserver.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final ParticipantsService participantsService;
    private final MessageBrokerService messageBrokerService;
    private final UserService userService;
    private final MediaService mediaService;
    private  final AccessService accessService;

    public ChatServiceImpl(ChatRoomService chatRoomService,
                           MessageService messageService,
                           ParticipantsService participantsService,
                           MessageBrokerService messageBrokerService,
                           UserService userService,
                           MediaService mediaService,
                           AccessService accessService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
        this.participantsService = participantsService;
        this.messageBrokerService = messageBrokerService;
        this.userService = userService;
        this.mediaService = mediaService;
        this.accessService = accessService;
    }

    @Override
    public ResponseFirstMessage sendNewMessage(InputMessage inputMessage, Integer senderId) throws IOException {
        if (inputMessage.getRecipientName() == null) {
            log.info("User with id = " + senderId + " send new message to chat room id: " + inputMessage.getChatId() );
        } else {
            log.info("User with id = " + senderId + " send new message to user: " + inputMessage.getRecipientName() );
        }
        log.info("message = " + inputMessage);
        Integer chatId, recipientId;

        ResponseFirstMessage responseFirstMessage = new ResponseFirstMessage();
        ChatNotification chatNotificationToSender = new ChatNotification();

        if (inputMessage.getChatId() != null) {
            //если в сообщении указан чат рум, то у такой уже был создан. Проверяем access и отправляем
            log.info("Chat id was setted");
            if (!accessService.checkUserAccessToChatRoom(inputMessage.getChatId(), senderId)) {
                log.info("For user with id = " + senderId + " access to chatroom with id = " + inputMessage.getChatId() + " denied");
                throw new ChatRoomException(ServerChatErrors.ACCESS_DENIED.name());
            }
            // находим получателя по чат руму
            recipientId = participantsService
                    .findRecipient(senderId, chatRoomService.findChatRoomById(inputMessage.getChatId())).getId();
            chatId = inputMessage.getChatId();
        } else {
            //находим получателя по имени в сообщении
            recipientId = userService.findUserByUserName(inputMessage.getRecipientName()).getId();
            // проверяем первое ли это сообщение
            if (participantsService.existsParticipantForPeerChat(recipientId, senderId)) {
                chatId = participantsService.findParticipantBySenderIdAndRecipientId(
                                senderId,
                                recipientId
                ).getChatRoom().getId();
                // TODO проверить не находится ли в блок листе
                //отправляем получателю уведомление о новом чат руме
            } else {
                ChatRoom chatRoom = chatRoomService.createChatRoomForPeerChat();

                participantsService
                        .createPeerChat(
                                senderId,
                                recipientId,
                                chatRoom
                        );
                chatId = chatRoom.getId();

                messageBrokerService.sendToUserChatRoom(
                        userService.findUserById(recipientId).getUserName(),
                        new ChatRoomNotification(
                                chatId
                        )
                );

                responseFirstMessage.setResponseChatRoom(
                        chatRoomService.serializeChatRoomToSend(chatRoom, senderId)
                );
            }
        }
        //chat id , sender id, recipient id
        //если удален у получателя, то отправляем ему чат рум
        if (chatRoomService.existsDeletedChatRoomByChatRoom_IdAndUser_Id(chatId, recipientId)) {
            log.info("Was deleted in recipient chat");
            messageBrokerService.sendToUserChatRoom(
                    userService.findUserById(senderId).getUserName(),
                    new ChatRoomNotification(
                            chatId
                    )
            );
            chatRoomService.removeDeletedChatRoomById(
                    chatRoomService.findDeletedChatRoomByChatRoomIdAndUserId(chatId, recipientId).getId()
            );
        }
        //если удален у отправителя, то отправляем ему чат рум
        if (chatRoomService.existsDeletedChatRoomByChatRoom_IdAndUser_Id(chatId, senderId)) {
            log.info("Was deleted in sender chat");
            responseFirstMessage.setResponseChatRoom(
                    chatRoomService.serializeChatRoomToSend(
                            chatRoomService.findChatRoomById(chatId),
                            senderId
                    )
            );
            chatRoomService.removeDeletedChatRoomById(
                    chatRoomService.findDeletedChatRoomByChatRoomIdAndUserId(chatId, senderId).getId()
            );
        }
        log.info("Create message");
        //создаем сообщение
        Message m = messageService.create(
                inputMessage,
                chatRoomService.findChatRoomById(chatId),
                userService.findUserById(senderId)
        );
        //отправляем
        messageBrokerService.sendToUserChatNotification(
                userService.findUserById(recipientId).getUserName(),
                new ChatNotification(
                        chatId,
                        m.getId(),
                        m.getSender().getUserName(),
                        m.getMedia() != null,
                        m.getDateCreate()
                )
        );
        chatNotificationToSender.setChatId(chatId);
        chatNotificationToSender.setMessageId(m.getId());
        chatNotificationToSender.setSenderName(m.getSender().getUserName());
        chatNotificationToSender.setHasMedia(m.getMedia() != null);
        chatNotificationToSender.setDateSend(m.getDateCreate());

        responseFirstMessage.setChatNotification(chatNotificationToSender);
        return responseFirstMessage;
    }
    @Override
    public ResponseMessageList getMessages(Integer chatId, Integer userId, Integer page, Integer pageSize) throws IOException {
        if (!accessService.checkUserAccessToChatRoom(chatId, userId)) {
            log.info("For user with id = " + userId + " access to chat room with id = " + chatId + " denied");
            throw new ChatRoomException(ServerChatErrors.ACCESS_DENIED.name());
        }

        log.info("User with id = " + userId + " get messages from chat with chat id = " + chatId);
        return messageService
                .serializeMessages(
                        messageService.findMessagesByChatIdPageable(chatId, page, pageSize),
                        userId
                );
    }

    @Override
    public String moveMessageToDeletedForUser(Integer messageId, Integer userId) {
        if (!accessService.checkUserAccessToMessage(messageId, userId)) {
            log.info("For user with id = " + userId + " access to message with id = " + messageId + " denied");
            throw new MessageException(ServerChatErrors.ACCESS_DENIED.name());
        }

        log.info("User with id = " + userId + " delete message with id = " + messageId);
        return messageService.moveMessageToDeleted(
                messageService.findMessageById(messageId),
                userService.findUserById(userId)
        );
    }

    @Override
    public String moveChatRoomToDeletedForUser(Integer chatId, Integer userId) {
        if (!accessService.checkUserAccessToChatRoom(chatId, userId)) {
            log.info("For user with id = " + userId + " access to chat room with id = " + chatId + " denied");
            throw new ChatRoomException(ServerChatErrors.ACCESS_DENIED.name());
        }

        log.info("User with id = " + userId + "delete chat room with id = " + chatId);
        messageService.moveMessagesToDeletedFromChatRoom(
                chatRoomService.moveChatRoomToDeletedByChatRoomAndUser(
                        chatRoomService.findChatRoomById(chatId),
                        userService.findUserById(userId)
                )
        );
        return MESSAGE_SUCCESSFUL_DELETED_CHAT_ROOM;
    }

    @Override
    public String getUserNameByPhone(String userPhone) {
        return userService.findUserByUserPhone(userPhone).getUserName();
    }

    @Override
    public void createGroupChat(InputGroupChat inputGroupChat) {
        if (inputGroupChat.getMembersNameList().size() == 0) {
            throw new ParticipantException(ServerChatErrors.NO_MEMBERS_IN_CHAT.name());
        }

        ChatRoom chatRoom = chatRoomService.createChatRoomForGroupChat(
                inputGroupChat.getNameGroupChat(),
                inputGroupChat.getBase64Image()
        );

        messageBrokerService.sendToUsersGroupChatRoom(
                participantsService.createGroupChat(
                        inputGroupChat.getMembersNameList(),
                        chatRoom
                ),
                chatRoom.getId()
        );
    }

    @Override
    public void sendNewGroupMessage(InputMessage inputMessage, Integer senderId) throws IOException {
        if (inputMessage.getChatId() == null) {
            throw new MessageException(ServerChatErrors.CHAT_ROOM_NOT_SEND.name());
        }
        messageBrokerService.sendToUsersGroupMessage(
                participantsService.findParticipantsByChatRoomId(inputMessage.getChatId()),
                messageService.serializeMessage(
                        messageService.create(
                                inputMessage,
                                chatRoomService.findChatRoomById(inputMessage.getChatId()),
                                userService.findUserById(senderId)
                        )
                )
        );
    }

    @Override
    public ResponseMedia getUserPhoto(Integer userId) {
        return null;
    }

    @Override
    public ResponseMessage getMessageById(Integer messageId, Integer userId) throws IOException {
        if (!accessService.checkUserAccessToMessage(messageId, userId)) {
            log.info("For user with id = " + userId + " access to message with id = " + messageId + " denied");
            throw new MessageException(ServerChatErrors.ACCESS_DENIED.name());
        }

        return messageService.serializeMessage(messageService.findMessageById(messageId));
    }

    @Override
    public ResponsePin pinImage(String imageBase64, Integer userId) throws IOException {
        return new ResponsePin(
                mediaService.serializeMediaFromBase64AndSave(
                        imageBase64,
                        userService.findUserById(userId).getUserName()
                ).getId()
        );
    }
}
