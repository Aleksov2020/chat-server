package com.app.chatserver.dto;

import java.util.Optional;

import com.app.chatserver.models.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ListOfChatRooms {
	private Optional<List<ChatRoom>> ChatRooms;

}
