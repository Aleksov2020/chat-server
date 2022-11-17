package com.app.chatserver.repo;

import com.app.chatserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUserName(String userName);

    Optional<User> findByPhone(String phone);

    boolean existsUserByUserName(String userName);
}
