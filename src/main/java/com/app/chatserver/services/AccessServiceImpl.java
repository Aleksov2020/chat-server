package com.app.chatserver.services;

import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.MessageException;
import org.springframework.stereotype.Service;

@Service
public class AccessServiceImpl implements AccessService{
    private final UserService userService;
    private final ParticipantsService participantsService;
    private final ChatRoomService chatRoomService;
    private final PostService postService;

    private final MessageService messageService;

    public AccessServiceImpl(UserService userService,
                             ParticipantsService participantsService,
                             ChatRoomService chatRoomService, PostService postService,
                             MessageService messageService){
        this.participantsService = participantsService;
        this.userService  = userService;
        this.chatRoomService = chatRoomService;
        this.postService = postService;
        this.messageService = messageService;
    }

    @Override
    public boolean checkUserAccessToMessage(Integer messageId, Integer userId) {
        return checkUserAccessToChatRoom(
                messageService.findMessageById(messageId).getChat().getId(),
                userId
        );
    }

    @Override
    public boolean checkUserAccessToChatRoom(Integer chatRoomId, Integer userId) {
        //if user delete chat room, but want to read deleted message -
        if (chatRoomService.existsDeletedChatRoomByChatRoom_IdAndUser_Id(chatRoomId, userId)) {
            return false;
        }
        //TODO if chat room was group and user was deleted - we have to check this
        return participantsService.existsParticipantByChatIdAndUserId(chatRoomId, userId);
    }

    @Override
    public boolean checkUserAccessToPost(Integer postId, Integer userId) {
        return false;
    }
}
