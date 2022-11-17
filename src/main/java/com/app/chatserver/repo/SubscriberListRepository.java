package com.app.chatserver.repo;

import com.app.chatserver.models.SubscriberList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SubscriberListRepository extends JpaRepository<SubscriberList, Integer> {
    Optional<SubscriberList> findById(Integer integer);
    Optional<SubscriberList> findSubscriberListBySubscriber_IdAndUser_Id(Integer subscriberId, Integer userId);
    List<SubscriberList> findSubscriberListsBySubscriber_Id(Integer subscriberId);
    boolean existsBySubscriber_IdAndUser_Id(Integer subscriberId, Integer userId);

}
