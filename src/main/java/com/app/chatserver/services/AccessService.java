package com.app.chatserver.services;

public interface AccessService {
    boolean checkUserAccessToMessage(Integer messageId, Integer userId);
    boolean checkUserAccessToChatRoom(Integer chatRoomId, Integer userId);
    boolean checkUserAccessToPost(Integer postId, Integer userId);
}
