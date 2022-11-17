package com.app.chatserver.services;

import com.app.chatserver.dto.ResponseSubscriber;
import com.app.chatserver.models.Post;
import com.app.chatserver.models.SubscriberList;
import com.app.chatserver.models.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SubscriberListService {
    String MESSAGE_SUCCESSFULLY_SUBSCRIBED = "SUCCESSFULLY_SUBSCRIBED";

    String MESSAGE_SUCCESSFULLY_DELETE_SUBSCRIBE = "SUCCESSFULLY_DELETE_SUBSCRIBE";

    String deleteSubscribe(SubscriberList subscriberList);

    String createSubscribe(User subscriber, User user);

    List<SubscriberList> findUsersThatUserHasSubscribed(Integer subscriberId);

    List<ResponseSubscriber> serializeSubscribersToResponse(List<SubscriberList> subscriberLists);

    SubscriberList findSubscriberListById(Integer id);

    SubscriberList findSubscriberListBySubscriberAndUser(User subscriber, User user);
}
