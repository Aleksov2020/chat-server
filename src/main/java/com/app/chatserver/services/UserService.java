package com.app.chatserver.services;

import com.app.chatserver.models.User;

public interface UserService {
    /**
     * Returns a User object wrapped Optional class
     *
     * @param  userName - String User Name
     * @return the user
     */
    User findUserByUserName(String userName);

    /**
     * Returns an User object wrapped Optional class
     *
     * @param  userId - Integer user id
     * @return the user
     */
    User findUserById(Integer userId);

    User findUserByUserPhone(String userPhone);

    /**
     * Returns boolean value. Check user exists in database
     *
     * @param  userId - Integer user id
     * @return the user
     */

    boolean checkUserExistsById(Integer userId);

    void incPostsCounter(User u);

    void decPostsCounter(User u);

    void incSubscribed(User u);

    void decSubscribed(User u);

    void incSubscribers(User u);

    void decSubscribers(User u);


}
