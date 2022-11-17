package com.app.chatserver.services;

import com.app.chatserver.dto.ResponseSubscriber;
import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.SubscribeException;
import com.app.chatserver.exceptions.UserException;
import com.app.chatserver.models.Post;
import com.app.chatserver.models.SubscriberList;
import com.app.chatserver.models.User;
import com.app.chatserver.repo.SubscriberListRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriberListServiceImpl implements SubscriberListService{
    private final SubscriberListRepository subscriberListRepository;

    private final UserService userService;

    public SubscriberListServiceImpl(SubscriberListRepository subscriberListRepository,
                                     UserService userService){
        this.subscriberListRepository = subscriberListRepository;
        this.userService = userService;
    }

    @Override
    public String deleteSubscribe(SubscriberList subscriberList) {
        subscriberListRepository.delete(subscriberList);

        userService.decSubscribed(subscriberList.getSubscriber());
        userService.decSubscribers(subscriberList.getUser());

        return MESSAGE_SUCCESSFULLY_DELETE_SUBSCRIBE;
    }

    @Override
    public String createSubscribe(User subscriber, User user) {
        if (subscriberListRepository.existsBySubscriber_IdAndUser_Id(subscriber.getId(), user.getId())) {
            throw new UserException(ServerChatErrors.ALREADY_SUBSCRIBED.name());
        }
        SubscriberList subscriberList = new SubscriberList();
        subscriberList.setSubscriber(subscriber);
        subscriberList.setUser(user);
        subscriberListRepository.save(subscriberList);

        userService.incSubscribed(subscriber);
        userService.incSubscribers(user);

        return MESSAGE_SUCCESSFULLY_SUBSCRIBED;
    }

    @Override
    public List<SubscriberList> findUsersThatUserHasSubscribed(Integer subscriberId) {
        return subscriberListRepository.findSubscriberListsBySubscriber_Id(subscriberId);
    }

    @Override
    public List<ResponseSubscriber> serializeSubscribersToResponse(List<SubscriberList> subscriberLists) {

        List<ResponseSubscriber> responseSubscriberList = new ArrayList<>();

        for (SubscriberList subscriberList : subscriberLists) {
            responseSubscriberList.add(
                    new ResponseSubscriber(
                            subscriberList.getUser().getUserName()
                    )
            );
        }

        return responseSubscriberList;
    }

    @Override
    public SubscriberList findSubscriberListById(Integer id) {
        return subscriberListRepository.findById(id).orElseThrow(
                () -> new SubscribeException(ServerChatErrors.SUBSCRIBE_NOT_FOUND.name())
        );
    }

    @Override
    public SubscriberList findSubscriberListBySubscriberAndUser(User subscriber, User user) {
        return subscriberListRepository.findSubscriberListBySubscriber_IdAndUser_Id(
                subscriber.getId(),
                user.getId()
        ).orElseThrow(
                () -> new SubscribeException(ServerChatErrors.SUBSCRIBE_NOT_FOUND.name())
        );
    }
}
