package com.app.chatserver.services;

import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.BlackListException;
import com.app.chatserver.models.BlackList;
import com.app.chatserver.models.User;
import com.app.chatserver.repo.BlackListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BlackListServiceImpl implements BlackListService{
    private final BlackListRepository blackListRepository;

    public BlackListServiceImpl(BlackListRepository blackListRepository){
        this.blackListRepository = blackListRepository;
    }

    @Override
    public BlackList findById(Integer blackListId) {
        return blackListRepository.findById(blackListId).orElseThrow(
                () -> new BlackListException(ServerChatErrors.BLACK_LIST_NOT_FOUND.name())
        );
    }

    @Override
    public List<BlackList> findByUserId(Integer userId) {
        return blackListRepository.findBlackListsByUser_Id(userId);
    }

    @Override
    public void create(User bannedUser, User user) {
        BlackList blackList = new BlackList();
        blackList.setUser(user);
        blackList.setBannedUser(bannedUser);
        blackListRepository.save(blackList);
    }
}
