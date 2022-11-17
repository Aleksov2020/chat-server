package com.app.chatserver.services;

import com.app.chatserver.models.BlackList;
import com.app.chatserver.models.User;

import java.util.List;
import java.util.Optional;

public interface BlackListService {
    BlackList findById(Integer blackListId);

    List<BlackList> findByUserId(Integer userId);

    void create(User bannedUser, User user);
}
