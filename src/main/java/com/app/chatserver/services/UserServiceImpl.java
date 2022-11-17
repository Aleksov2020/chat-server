package com.app.chatserver.services;

import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.UserException;
import com.app.chatserver.models.User;
import com.app.chatserver.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName).orElseThrow(
                () -> new UserException(ServerChatErrors.USER_NOT_FOUND.name())
        );
    }
    
    public User findUserById(Integer userId) {
    	return userRepository.findById(userId).orElseThrow(
                () -> new UserException(ServerChatErrors.USER_NOT_FOUND.name())
        );
    }

    @Override
    public User findUserByUserPhone(String userPhone) {
        return userRepository.findByPhone(userPhone).orElseThrow(
                () -> new UserException(ServerChatErrors.USER_NOT_FOUND.name())
        );
    }

    @Override
    public boolean checkUserExistsById(Integer userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public void incPostsCounter(User u) {
        u.setPostsCounter(u.getPostsCounter() + 1);
        userRepository.save(u);
    }

    @Override
    public void decPostsCounter(User u) {
        u.setPostsCounter(u.getPostsCounter() - 1);
        userRepository.save(u);
    }

    @Override
    public void incSubscribed(User u) {
        u.setSubscribedCounter(u.getSubscribedCounter() + 1);
        userRepository.save(u);
    }

    @Override
    public void decSubscribed(User u) {
        u.setSubscribedCounter(u.getSubscribedCounter() - 1);
        userRepository.save(u);
    }

    @Override
    public void incSubscribers(User u) {
        u.setSubscriberCounter(u.getSubscriberCounter() + 1);
        userRepository.save(u);
    }

    @Override
    public void decSubscribers(User u) {
        u.setSubscriberCounter(u.getSubscriberCounter() - 1);
        userRepository.save(u);
    }


}
