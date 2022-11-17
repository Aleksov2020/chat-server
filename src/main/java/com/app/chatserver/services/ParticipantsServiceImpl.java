package com.app.chatserver.services;

import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.ChatRoomException;
import com.app.chatserver.exceptions.ParticipantException;
import com.app.chatserver.exceptions.UserException;
import com.app.chatserver.models.ChatRoom;
import com.app.chatserver.models.Participant;
import com.app.chatserver.models.User;
import com.app.chatserver.repo.ParticipantsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ParticipantsServiceImpl implements ParticipantsService {
    private final ParticipantsRepository participantsRepository;
    private final UserService userService;
    public ParticipantsServiceImpl(ParticipantsRepository participantsRepository,
                                   UserService userService){
        this.participantsRepository = participantsRepository;
        this.userService = userService;
    }

    @Override
    public void createPeerChat(Integer senderId, Integer recipientId, ChatRoom chatRoom) {
        Participant participant = new Participant();
        participant.setUser(userService.findUserById(senderId));
        participant.setChatRoom(chatRoom);
        participantsRepository.save(participant);

        Participant participant2 = new Participant();
        participant2.setUser(userService.findUserById(recipientId));
        participant2.setChatRoom(chatRoom);
        participantsRepository.save(participant2);
    }

    @Override
    public boolean existsParticipantForPeerChat(Integer senderId, Integer recipientId) {
        return participantsRepository.findParticipantBySenderIdAndRecipientId(senderId, recipientId).isPresent();
    }

    @Override
    public List<Participant> findParticipantsByChatRoomId(Integer chatRoomId) {
        return participantsRepository.findParticipantsByChatRoom_Id(chatRoomId);
    }

    @Override
    public List<Participant> findAllByUserId(Integer userId, Integer page, Integer pageSize) {
        return participantsRepository.findAllByUserId(userId, pageSize, page*pageSize);
    }

    @Override
    public User findRecipient(Integer senderId, ChatRoom chatRoom) {
        List<Participant> participantList = findParticipantsByChatRoomId(chatRoom.getId());
        Integer recipientId;
        if (participantList.get(0).getUser().getId() != senderId) {
            recipientId = participantList.get(0).getUser().getId();
        } else {
            recipientId = participantList.get(1).getUser().getId();
        }
        log.info("Find user with id = " + recipientId);
        return userService.findUserById(recipientId);
    }

    @Override
    public List<Participant> createGroupChat(List<String> membersNameList, ChatRoom chatRoom) {
        List<Participant> participantList = new ArrayList<>();
        for (String userName : membersNameList) {
            Participant participant = new Participant();
            participant.setUser(userService
                    .findUserByUserName(userName)
            );
            participant.setChatRoom(chatRoom);
        }
        return participantsRepository.saveAll(participantList);
    }

    @Override
    public List<User> findListOfParticipantInChat(Integer userId, Integer chatId) {
        List<User> participantNameList = new ArrayList<>();
        for (Participant p : participantsRepository.findParticipantsByChatRoom_Id(chatId)){
            if (!p.getUser().getId().equals(userId)){
                participantNameList.add(p.getUser());
            }
        }
        return participantNameList;
    }

    @Override
    public Participant findParticipantBySenderIdAndRecipientId(Integer senderId, Integer recipientId) {
        //return sender's Participant
        return participantsRepository.findParticipantBySenderIdAndRecipientId(senderId, recipientId).orElseThrow(
                () -> new ParticipantException(ServerChatErrors.CHAT_ROOM_NOT_FOUND.name())
        );
    }

    @Override
    public boolean existsParticipantByChatIdAndUserId(Integer chatId, Integer userId) {
        return participantsRepository.existsParticipantByChatRoom_IdAndUser_Id(chatId, userId);
    }
}
