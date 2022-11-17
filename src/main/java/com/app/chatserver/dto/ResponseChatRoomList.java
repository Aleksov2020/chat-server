package com.app.chatserver.dto;

import com.app.chatserver.models.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseChatRoomList {
    private List<ResponseChatRoom> responseChatRoomList;
}
