package com.app.chatserver.services;

import com.app.chatserver.dto.*;
import com.app.chatserver.models.ChatRoom;
import com.app.chatserver.models.Message;
import com.app.chatserver.models.Participant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MessageBrokerServiceImpl implements MessageBrokerService {
    private final SimpMessagingTemplate messagingTemplate;

    public MessageBrokerServiceImpl(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendToUsersChatRoom(String channelSender, String channelRecipient, ResponseChatRoom responseChatRoom) {
        log.info("Send to channel " + channelSender + "/queue/message" + " message: " + responseChatRoom.toString());
        log.info("Send to channel " + channelRecipient + "/queue/message" + " message: " + responseChatRoom.toString());
        messagingTemplate.convertAndSendToUser(
                channelSender, "/new/chatRoom",
                responseChatRoom
        );
        messagingTemplate.convertAndSendToUser(
                channelRecipient, "/new/chatRoom",
                responseChatRoom
        );
    }

    @Override
    public void sendToUserChatRoom(String channelSender, ChatRoomNotification chatRoomNotification) {
        messagingTemplate.convertAndSendToUser(
                channelSender, "/new/chatRoom",
                chatRoomNotification
        );
    }

    @Override
    public void sendToUsersPost(String channelRecipient, ResponsePost responsePost) {
        log.info("Send to channel " + channelRecipient + "/new/post" + " message: " + responsePost.toString());
        messagingTemplate.convertAndSendToUser(
                channelRecipient,"/new/post",
                responsePost
        );
    }
    //TODO test
    @Override
    public void sendToUsersGroupChatRoom(List<Participant> participantList, Integer chatRoomId) {
        log.info("New group chat room. Chat room id = " + chatRoomId);
        for (Participant participant : participantList) {
            messagingTemplate.convertAndSendToUser(
                participant.getUser().getId().toString(), "/new/chatRoom",
                chatRoomId
            );
        }
    }

    @Override
    public void sendToUserPostNotification(String channelRecipient, PostNotification postNotification) {
        log.info("Send to channel " + channelRecipient + "/new/post" + " message: " + postNotification.toString());
        messagingTemplate.convertAndSendToUser(
                channelRecipient,"/new/post",
                postNotification
        );
    }

    @Override
    public void sendToUserChatNotification(String channelRecipient, ChatNotification chatNotification) {
        log.info("Send to channel " + channelRecipient + "/queue/message" + " message: " + chatNotification.toString());
        messagingTemplate.convertAndSendToUser(
                channelRecipient,
                "/queue/message",
                chatNotification
        );
    }

    //TODO test
    @Override
    public void sendToUsersGroupMessage(List<Participant> participantList, ResponseMessage responseMessage) {
        for (Participant participant : participantList) {
            log.info("Send to channel " + participant.getUser().getId() + "/queue/message" + " message: " + responseMessage.toString());
            messagingTemplate.convertAndSendToUser(
                    participant.getUser().getId().toString(),
                    "/queue/message",
                    responseMessage
            );
        }
    }
}
