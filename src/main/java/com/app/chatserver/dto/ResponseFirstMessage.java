package com.app.chatserver.dto;

import lombok.Data;

@Data
public class ResponseFirstMessage {
    private ChatNotification chatNotification;
    private ResponseChatRoom responseChatRoom;
}
