package com.app.chatserver.services;

import com.app.chatserver.dto.*;
import com.app.chatserver.models.Message;
import com.app.chatserver.models.Participant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageBrokerService {
    void sendToUsersChatRoom(String channelSender, String channelRecipient, ResponseChatRoom responseChatRoom);
    void sendToUserChatRoom(String channelSender, ChatRoomNotification chatRoomNotification);
    void sendToUsersPost(String channelRecipient, ResponsePost responsePost);
    void sendToUsersGroupChatRoom(List<Participant> participantList, Integer chatRoomId);
    void sendToUserPostNotification(String channelRecipient, PostNotification postNotification);
    void sendToUserChatNotification(String channelRecipient, ChatNotification chatNotification);
    void sendToUsersGroupMessage(List<Participant> participantList, ResponseMessage responseMessage);
}
